package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopService;
import lk.ijse.gdse.traditionalexperiencebackend.util.ResponseUtil;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workshop")
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    @PostMapping(value ="addWorkshop" , consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> saveInstructor(@RequestPart("workshop") WorkshopDTO workshopDTO,
                                                      @RequestPart("file") MultipartFile[] multipartFiles) throws IOException {

        try {
            List<String> fileNames = new ArrayList<>();
            Path uploadDir = Paths.get("uploads/");
            Files.createDirectories(uploadDir);

            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                Path filePath = uploadDir.resolve(fileName);
                Files.write(filePath, multipartFile.getBytes());
                fileNames.add(fileName);
            }
            workshopDTO.setImage(fileNames);

            int response = workshopService.addWorkshop(workshopDTO);
            if (response == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Workshop Saved", workshopDTO));
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

    @GetMapping("/getAllWorkshops")
    public ResponseEntity<ResponseDTO> getAllWorkshops() {
        try{
            List<WorkshopDTO> workshops = workshopService.getAllWorkshops();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK,"suceess", workshops));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getWorkshopByItem/{itemId}")
    public ResponseEntity<ResponseDTO> getWorkshopByItem(@PathVariable Long itemId){
        try{
            List<WorkshopDTO> workshops = workshopService.getWorkshopsByItem(itemId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK, "Success", workshops));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @GetMapping("/getWorkshopById/{id}")
    public ResponseEntity<ResponseDTO> getWorkshopById(@PathVariable Long id){
        try{
            WorkshopDTO workshopDTO = workshopService.getWorkshopById(id);
            if(workshopDTO != null){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Success", workshopDTO));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Workshop Not Found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @GetMapping("/getParticipantCount/{id}")
    public ResponseEntity<ResponseDTO> getParticipantCount(@PathVariable Long id){
        try{
            int count = workshopService.getParticipantsById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK, "Success", count));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @PutMapping( value = "/updateWorkshop",  consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> updateWorkshop(@RequestPart("workshop") WorkshopDTO workshopDTO
            , @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles) throws IOException {
        try {
            List<String> fileNames = new ArrayList<>();
            Path uploadDir = Paths.get("uploads/");
            Files.createDirectories(uploadDir);

            if(multipartFiles != null && multipartFiles.length > 0) {


                for (MultipartFile multipartFile : multipartFiles) {
                    String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                    Path filePath = uploadDir.resolve(fileName);
                    Files.write(filePath, multipartFile.getBytes());
                    fileNames.add(fileName);
                }
                if (workshopDTO.getImage() != null) {
                    fileNames.addAll(workshopDTO.getImage());
                }
                workshopDTO.setImage(fileNames);
            }else {
                workshopDTO.setImage(workshopDTO.getImage());
            }



            int response = workshopService.updateWorkshop(workshopDTO);
            switch (response) {
                case VarList.Updated -> {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Updated, "Instructor Saved", workshopDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Instructor Already exists", null));

                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error While Saving", null));

                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"file Upload Failed" + e.getMessage(),null));


        }
    }

    @DeleteMapping("/deleteWorkshop/{id}")
    public ResponseEntity<ResponseDTO> deleteWorkshop(@PathVariable Long id){
        try{
            boolean deleted = workshopService.deleteWorkshop(id);
            if(deleted){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Instructor Deleted", null));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Instructor Not Found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
