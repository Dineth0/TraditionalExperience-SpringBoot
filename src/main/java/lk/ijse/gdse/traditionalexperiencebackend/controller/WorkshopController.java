package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopService;
import lk.ijse.gdse.traditionalexperiencebackend.util.ResponseUtil;
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

@RestController
@RequestMapping("/api/v1/workshop")
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    @PostMapping(value = "/addWorkshop", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseUtil> saveWorkshop(@RequestPart("workshop") WorkshopDTO workshopDTO,
                                                     @RequestPart("file") MultipartFile multipartFile) throws IOException {

        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        Path filePath = Paths.get("uploads/", fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, multipartFile.getBytes());

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
        workshopDTO.setImage(fileName);
        workshopService.addWorkshop(workshopDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseUtil(200,"Added",workshopDTO));

    }
}
