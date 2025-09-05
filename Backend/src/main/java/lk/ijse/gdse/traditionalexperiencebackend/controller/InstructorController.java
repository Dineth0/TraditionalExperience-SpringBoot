package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.service.InstructorService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/instructor")
@CrossOrigin
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @PostMapping(value ="addInstructor" , consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> saveInstructor(@RequestPart("instructor") InstructorDTO instructorDTO,
                                                      @RequestPart("file") MultipartFile multipartFile) throws IOException {

        try {
            String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            Path filePath = Paths.get("uploads/", fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, multipartFile.getBytes());
            instructorDTO.setImage(fileName);

            int response = instructorService.addInstructor(instructorDTO);
            switch (response) {
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Instructor Saved", instructorDTO));
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
    @GetMapping("/getAllInstructors")
    public ResponseEntity<ResponseDTO> getAllInstructors() {
        try{
            List<InstructorDTO> instructors = instructorService.getAllInstructors();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK,"suceess", instructors));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @PutMapping( value = "/updateInstructor",  consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> updateInstructor(@RequestPart("instructor") InstructorDTO instructorDTO
    , @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                Path filePath = Paths.get("uploads/", fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, multipartFile.getBytes());
                instructorDTO.setImage(fileName);
            }



            int response = instructorService.updateInstructor(instructorDTO);
            switch (response) {
                case VarList.Updated -> {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Instructor Saved", instructorDTO));
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
    @GetMapping("/getInstructorById/{id}")
    public ResponseEntity<ResponseDTO> getInstructorById(@PathVariable Long id) {
        try{
            InstructorDTO instructorDTO = instructorService.getInstructorById(id);
            if(instructorDTO != null){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Success", instructorDTO));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Workshop Not Found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @DeleteMapping("/deleteInstructor/{id}")
    public ResponseEntity<ResponseDTO> deleteInstructor(@PathVariable Long id) {
        try{
            boolean deleted = instructorService.deleteInstructor(id);
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
