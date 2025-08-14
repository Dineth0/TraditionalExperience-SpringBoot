package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import lk.ijse.gdse.traditionalexperiencebackend.service.TraditionalItemService;
import lk.ijse.gdse.traditionalexperiencebackend.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/item")
@CrossOrigin
public class TraditionalItemController {
    @Autowired
    private TraditionalItemService itemService;

    @PostMapping(value = "/addItem", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseUtil> saveItem(@RequestPart("item") TraditionalItemDTO itemDTO,
                                                 @RequestPart("file") MultipartFile[] multipartFiles) throws IOException {
        List<String> fileNames = new ArrayList<>();
        Path uploadDir = Paths.get("uploads/");
        Files.createDirectories(uploadDir);

        for(MultipartFile multipartFile : multipartFiles) {

            String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            Files.write(filePath, multipartFile.getBytes());
            fileNames.add(fileName);
        }


        itemDTO.setItemImage(fileNames);
        itemService.addItem(itemDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseUtil(200,"Added",itemDTO));
    }

    @GetMapping("/getAllItems")
    public ResponseEntity<List<TraditionalItemDTO>> getAllItems(){
        List<TraditionalItemDTO> items = itemService.getAllItems();
        if(items.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/getItem/{id}")
    public ResponseEntity<TraditionalItemDTO> getItemById(@PathVariable UUID id){
        TraditionalItemDTO item = itemService.getItemById(id);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

}
