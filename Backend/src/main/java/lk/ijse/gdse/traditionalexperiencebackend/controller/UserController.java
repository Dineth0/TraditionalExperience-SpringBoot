package lk.ijse.gdse.traditionalexperiencebackend.controller;



import jakarta.servlet.http.HttpServletRequest;
import lk.ijse.gdse.traditionalexperiencebackend.config.JwtFilter;
import lk.ijse.gdse.traditionalexperiencebackend.dto.*;
import lk.ijse.gdse.traditionalexperiencebackend.entity.User;
import lk.ijse.gdse.traditionalexperiencebackend.repo.UserRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.UserService;
import lk.ijse.gdse.traditionalexperiencebackend.util.JwtUtil;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
//@CrossOrigin(origins = "*")
@Validated
@CrossOrigin
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserRepo userRepo;



    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> addUser(@RequestBody UserDTO userDTO) {
        System.out.println("register");
        System.out.println(userDTO.getEmail());
        System.out.println(userDTO.getUsername());
        System.out.println(userDTO.getRole());
        try {
            int res = userService.addUser(userDTO);
            switch (res) {
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }




    @GetMapping("/getUserIdFromToken")
    public ResponseEntity<?> getUserId(@RequestHeader("Authorization") String token) {
        try {

            String jwt = token.replace("Bearer ", "");
            String userId = jwtFilter.extractUserId(jwt);


            Map<String, String> response = new HashMap<>();
            response.put("userId", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
    }



    @GetMapping("/getByEmail")

    public ResponseEntity<?> getUserIdByEmail(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String email = jwtFilter.extractEmailFromToken(token);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email not found in token");
        }


        User user = userService.findByEmail(email);
        if (user != null) {
            Map<String, Object> users = new HashMap<>();
            users.put("userId", user.getId());
            users.put("username", user.getUsername());
            users.put("email", user.getEmail());
            return ResponseEntity.ok(users);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @GetMapping("/paginated")
    public List<UserDTO> getPaginatedUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        return userService.getUsersForPage(page, size);
    }

    @GetMapping("/total-pages")
    public int getTotalPages(@RequestParam(defaultValue = "2") int size) {
        return userService.getTotalPages(size);
    }


//    @GetMapping("/searchUsers/{keyword}")
//    public ResponseEntity<ResponseDTO> searchUsers(@PathVariable("keyword") String keyword) {
//        List<UserDTO> users = userService.searchUsers(keyword);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseDTO(VarList.OK, "Search Success", users));
//    }

    @GetMapping("/searchUsersByRole/{keyword}")
    public ResponseEntity<ResponseDTO> searchUsersByRole(@PathVariable("keyword") String keyword) {
        List<UserDTO> users = userService.searcherUsersByRole(keyword);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(VarList.OK, "Search Success", users));
    }
}
