package lk.ijse.gdse.traditionalexperiencebackend.service.impl;


import lk.ijse.gdse.traditionalexperiencebackend.dto.UserDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.RoleType;
import lk.ijse.gdse.traditionalexperiencebackend.entity.User;
import lk.ijse.gdse.traditionalexperiencebackend.repo.UserRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.UserService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> otpStorage = new HashMap<>();

    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return "User not found!";
        }
        String otp = generateOTP();
        otpStorage.put(email, otp);
        sendEmail(email, otp);
        return "OTP sent to your email";
    }

    public String verifyOtp(String email, String otp) {
        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            otpStorage.remove(email);
            return "OTP verified. You can reset your password.";
        }
        return "Invalid OTP";
    }

    public String resetPassword(String email, String newPassword) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return "User not found!";

        }

        // BCrypt encoder object hadanna
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // New password eka hash karanna
        String hashedPassword = encoder.encode(newPassword);

        // Hashed password eka database ekata save karanna
        user.setPassword(hashedPassword);
        userRepo.save(user);
        return "Password updated successfully!";
    }

    private String generateOTP() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private void sendEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }



    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepo.findByEmail(username);
        return modelMapper.map(user,UserDTO.class);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return authorities;
    }

    @Override
    public int addUser(UserDTO userDTO) {
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            return VarList.Not_Acceptable;
        } else {
            // Map UserDTO to User entity
            User user = modelMapper.map(userDTO, User.class);

            // Encrypt the password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));


            // Set default role if not provided
            if (user.getRole() == null) {
                user.setRole(RoleType.USER); // Default role
            }

            userRepo.save(user);
            // Return success response
            return VarList.Created;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Use email as the username
                user.getPassword(), // Password
                getAuthority(user) // Convert role to GrantedAuthority
        );
    }




    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }







}
