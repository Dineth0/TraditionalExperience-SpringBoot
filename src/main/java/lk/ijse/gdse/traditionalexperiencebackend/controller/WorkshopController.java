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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error While Saving", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"file Upload Failed" + e.getMessage(),null));


        }
    }
}
