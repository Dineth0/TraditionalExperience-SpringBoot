package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopRegistrationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopRegistrationService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/registerWorkshop")
    public ResponseEntity<ResponseDTO> registerWorkshop(@RequestBody WorkshopRegistrationDTO workshopRegistrationDTO){
        try {
            int response = workshopRegistrationService.registerWorkshop(workshopRegistrationDTO);

            switch (response){
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Registration successfully",workshopRegistrationDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "This time has Booked",null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error",null));
                }
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(),null));
        }
    }
    @GetMapping("/checkAvailability/{workshopId}")
    public ResponseEntity<?> checkAvailability (@PathVariable Long workshopId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        List<Map<String, Object>> availability = workshopRegistrationService.checkAvailability(workshopId, date);
        return ResponseEntity.ok(availability);
    }
}
