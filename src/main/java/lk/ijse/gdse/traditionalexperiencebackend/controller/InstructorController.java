package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.service.InstructorService;
import lk.ijse.gdse.traditionalexperiencebackend.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/instructor")
@CrossOrigin
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @PostMapping(value ="addInstructor" , consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseUtil> saveInstructor(@RequestPart("instructor") InstructorDTO instructorDTO,
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
        instructorDTO.setImage(fileName);
        instructorService.addInstructor(instructorDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseUtil(200,"Added",instructorDTO));

    }
}
