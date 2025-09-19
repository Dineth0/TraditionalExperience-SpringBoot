package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.*;
import lk.ijse.gdse.traditionalexperiencebackend.service.NotificationService;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopRegistrationService;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/workshopRegistration")
@CrossOrigin
public class WorkshopRegistrationController {

    @Autowired
    private WorkshopRegistrationService workshopRegistrationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WorkshopService workshopService;

    @PostMapping("/registerWorkshop")
    public ResponseEntity<ResponseDTO> registerWorkshop(@RequestBody WorkshopRegistrationDTO workshopRegistrationDTO){
        try{
            WorkshopRegistrationDTO savedDTO = workshopRegistrationService.registerWorkshop(workshopRegistrationDTO);

            if(savedDTO != null){
                NotificationDTO notificationDTO = new NotificationDTO();
                WorkshopDTO workshopDTO = workshopService.getWorkshopById(workshopRegistrationDTO.getWorkshopId());
                notificationDTO.setMessage("New Workshop Booking For " + workshopDTO.getTitle() );
                Long adminUserId = 2L;
                notificationDTO.setUserId(adminUserId);
                notificationDTO.setReadStatus(false);
                notificationDTO.setCreateAt(new  java.sql.Date(System.currentTimeMillis()));

                notificationService.createNotification(notificationDTO);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Registration successfully", savedDTO));
            }else {

                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "This time has not found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Booking Deleted", null));
        }
    }
    @GetMapping("/checkAvailability/{workshopId}")
    public ResponseEntity<?> checkAvailability (@PathVariable Long workshopId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        List<Map<String, Object>> availability = workshopRegistrationService.checkAvailability(workshopId, date);
        return ResponseEntity.ok(availability);
    }

    @DeleteMapping("/cancelBooking/{id}")
    public ResponseEntity<ResponseDTO> cancelBooking (@PathVariable Long id){
        try{
            boolean deleted = workshopRegistrationService.cancelBooking(id);
            if (deleted){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Booking Deleted", null));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Booking Not Found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/myBooking/{userId}")
    public ResponseEntity<ResponseDTO> myBooking (@PathVariable Long userId){
        List<WorkshopRegistrationDTO> bookings = workshopRegistrationService.getBookingsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(VarList.OK, "Success", bookings));
    }

    @GetMapping("/getAllRegistrations")
    public ResponseEntity<ResponseDTO> getAllRegistrations(){
        try{
            List<WorkshopRegistrationDTO> workshopRegistrations = workshopRegistrationService.getAlRegistrations();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK,"suceess", workshopRegistrations));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
//    @GetMapping("/searchRegistrations/{keyword}")
//    public ResponseEntity<ResponseDTO> searchRegistrations(@PathVariable("keyword") int keyword){
//        try {
//            List<WorkshopRegistrationDTO> workshopRegistrationDTO = workshopRegistrationService.searchWorkshops(keyword);
//
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseDTO(VarList.OK, "suceess", workshopRegistrationDTO));
//
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
//        }
//
//    }


    @GetMapping("/searchWorkshopBookings/{date}")
    public ResponseEntity<ResponseDTO> searchWorkshopBookings(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.sql.Date date){
        List<WorkshopRegistrationDTO> workshopRegistrations = workshopRegistrationService.searchWorkshopRegistrations(date);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(VarList.OK, "Search Success", workshopRegistrations));
    }

   @GetMapping("/summary/workshop-wise")
    public ResponseEntity<List<Map<String, Object>>> WorkshopWiseSummary(){
        return ResponseEntity.ok(workshopRegistrationService.getWorkshopBookingCounts());
   }

    @GetMapping("/RegistrationPagination")
    public ResponseEntity<ResponseDTO> pagination(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {

        try{
            Pageable pageable = PageRequest.of(page, size);
            Page<WorkshopRegistrationDTO> workshopRegistrations = workshopRegistrationService.getWorkshopRegistrationsForPAge(pageable);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK, "Page Loaded", workshopRegistrations));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Instructor Deleted", null));
        }
    }
    @GetMapping("/total-pages")
    public int getTotalPages(@RequestParam(defaultValue = "3") int size) {
        return workshopRegistrationService.getTotalPages(size);
    }

}
