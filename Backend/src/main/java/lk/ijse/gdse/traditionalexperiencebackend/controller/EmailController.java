package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.BookingEmailDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.EmailDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Email;
import lk.ijse.gdse.traditionalexperiencebackend.service.EmailService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendMail")
    public ResponseEntity<ResponseDTO> sendEmail(@RequestBody BookingEmailDTO bookingEmailDTO) {

        try{
            if(bookingEmailDTO.getRegistrationId() == null){
                return ResponseEntity.status((HttpStatus.BAD_REQUEST))
                        .body(new ResponseDTO(VarList.Bad_Gateway, "RegistrationId cannot b null", null));

            }
            Email savedEmail = emailService.sendBookingDetailsEmail(
                    bookingEmailDTO.getRegistrationId(),
                    bookingEmailDTO.getInstructorEmail()
            );

            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setId(savedEmail.getId());
            emailDTO.setSubject(savedEmail.getSubject());
            emailDTO.setBody(savedEmail.getBody());
            emailDTO.setCreatedAt(savedEmail.getCreatedAt());
            if(savedEmail.getInstructor() != null){
                emailDTO.setInstructorId(savedEmail.getInstructor().getId());
            }
            return ResponseEntity.status((HttpStatus.CREATED))
                    .body(new ResponseDTO(VarList.Created, "RegistrationId cannot b null", emailDTO));

        }catch (RuntimeException e){
            return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR))
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "RegistrationId cannot b null", e.getMessage()));

        }
    }

}
