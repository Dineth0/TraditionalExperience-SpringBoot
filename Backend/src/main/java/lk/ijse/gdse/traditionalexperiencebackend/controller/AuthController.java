
package lk.ijse.gdse.traditionalexperiencebackend.controller;



import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import jakarta.validation.Valid;
import lk.ijse.gdse.traditionalexperiencebackend.dto.AuthDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.UserDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.RoleType;
import lk.ijse.gdse.traditionalexperiencebackend.entity.User;
import lk.ijse.gdse.traditionalexperiencebackend.service.impl.UserServiceImpl;
import lk.ijse.gdse.traditionalexperiencebackend.util.JwtUtil;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;


import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;

    private static final String GOOGLE_CLIENT_ID = "49054632989-miqc6erkgq84104g1g18m33ifjtdk35f.apps.googleusercontent.com";



    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserServiceImpl userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;

    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDTO> authenticate( @RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(VarList.Unauthorized, "Invalid Credentials", e.getMessage()));
        }

        UserDTO loadedUser = userService.loadUserDetailsByUsername(userDTO.getEmail());
        if (loadedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        String token = jwtUtil.generateToken(loadedUser);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(loadedUser.getEmail());
        authDTO.setToken(token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(VarList.Created, "Success", authDTO));
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(userService.verifyOtp(email, otp));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");
        return ResponseEntity.ok(userService.resetPassword(email, newPassword));
    }

    @PostMapping("/google")
    public ResponseEntity<ResponseDTO> googleLogin(@RequestBody Map<String, String> payload) {
        String idTokenString = payload.get("authtoken");

        try {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();


            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload tokenPayload = idToken.getPayload();
                String email = tokenPayload.getEmail();
                String name = (String) tokenPayload.get("username");


                User user = userService.findByEmail(email);
                if (user == null) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(email);
                    userDTO.setUsername(name);
                    userDTO.setRole(RoleType.USER);
                    userService.addUser(userDTO);
                }


                UserDTO loadedUser = userService.loadUserDetailsByUsername(email);
                String jwtToken = jwtUtil.generateToken(loadedUser);

                AuthDTO authDTO = new AuthDTO();
                authDTO.setEmail(email);
                authDTO.setToken(jwtToken);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Login Success", authDTO));

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseDTO(VarList.Unauthorized, "Invalid Google Token", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }







}
