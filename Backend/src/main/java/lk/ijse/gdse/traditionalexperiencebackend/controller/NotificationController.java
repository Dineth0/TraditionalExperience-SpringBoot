package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.NotificationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.service.NotificationService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@CrossOrigin
@RequiredArgsConstructor
public class NotificationController {


    private final NotificationService notificationService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        try {

            int response = notificationService.createNotification(notificationDTO);
            if (response == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Workshop Saved", notificationDTO));
            }else {
                System.out.println(response);
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error While Saving", null));

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"file Upload Failed" + e.getMessage(),null));


        }
    }
    @GetMapping("/unread/{userId}")
    public ResponseEntity<ResponseDTO> getUnreadNotifications(@PathVariable Long userId) {
        try { 
            return ResponseEntity.ok(
                    new ResponseDTO(VarList.OK, "Success",
                            notificationService.getUnReadNotifications(userId))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @PutMapping("/markAsRead/{userId}")
    public ResponseEntity<ResponseDTO> markAsRead(@PathVariable Long userId) {

        try {
            int updateCount = notificationService.markAsRead(userId);
            return ResponseEntity.ok(
                    new ResponseDTO(VarList.OK, updateCount + "success", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @PutMapping("/markSingle/{notificationId}")
    public ResponseEntity<ResponseDTO> markSingle(@PathVariable Long notificationId) {
        try {
            notificationService.markSingleAsRead(notificationId);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Marked one", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }


}
