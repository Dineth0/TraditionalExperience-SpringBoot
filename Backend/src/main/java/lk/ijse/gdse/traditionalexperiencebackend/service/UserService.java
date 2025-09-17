package lk.ijse.gdse.traditionalexperiencebackend.service;


import lk.ijse.gdse.traditionalexperiencebackend.dto.PaymentDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.UserDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {


    int addUser(UserDTO userDTO);

    UserDetails loadUserByUsername(String email);



    User findByEmail(String email);

    public List<UserDTO> getUsersForPage(int page, int size);

    public int getTotalPages(int size);

//    public List<UserDTO> searchUsers(String keyword);

    public List<UserDTO>searcherUsersByRole(String keyword);




}
