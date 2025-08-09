package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import lk.ijse.gdse.traditionalexperiencebackend.service.TraditionalItemService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
@CrossOrigin
public class TraditionalItemController {
    @Autowired
    private TraditionalItemService itemService;

    @PostMapping(value = "/addItem", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseUtil> saveItem(@RequestPart("item") TraditionalItemDTO itemDTO,
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
        itemDTO.setItemImage(fileName);
        itemService.addItem(itemDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseUtil(200,"Added",itemDTO));
    }

    @GetMapping("/getAllItems")
    public ResponseEntity<?> getAllItems() {
        List<TraditionalItemDTO> items = itemService.getAllItems();
        if(items.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseUtil(204,"No Items Found",null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }
}
