package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import lk.ijse.gdse.traditionalexperiencebackend.service.TraditionalItemService;
import lk.ijse.gdse.traditionalexperiencebackend.util.ResponseUtil;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ResponseDTO> saveItem(@RequestPart("item") TraditionalItemDTO itemDTO,
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
            itemDTO.setItemImage(fileNames);

            int response = itemService.addItem(itemDTO);
            if (response == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Workshop Saved", itemDTO));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error While Saving", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"file Upload Failed" + e.getMessage(),null));


        }
    }

    @PutMapping( value = "/updateItem",  consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> updateWorkshop(@RequestPart("item") TraditionalItemDTO traditionalItemDTO
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
                if (traditionalItemDTO.getItemImage() != null) {
                    fileNames.addAll(traditionalItemDTO.getItemImage());
                }
                traditionalItemDTO.setItemImage(fileNames);
            }else {
                traditionalItemDTO.setItemImage(traditionalItemDTO.getItemImage());
            }



            int response = itemService.updateItem(traditionalItemDTO);
            switch (response) {
                case VarList.Updated -> {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Updated, "Instructor Saved", traditionalItemDTO));
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

    @GetMapping("/getAllItems")
    public ResponseEntity<ResponseDTO> getAllItems(){
        try{
            List<TraditionalItemDTO> traditionalItems = itemService.getAllItems();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK,"suceess", traditionalItems));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getItem/{id}")
    public ResponseEntity<ResponseDTO> getItemById(@PathVariable Long id){
        try{
            TraditionalItemDTO traditionalItemDTO = itemService.getItemById(id);
            if(traditionalItemDTO != null){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Success", traditionalItemDTO));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Workshop Not Found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/paginated")
    public List<TraditionalItemDTO> getPaginatedItems(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        return itemService.getItemsForPage(page, size);
    }

    @GetMapping("/total-pages")
    public int getTotalPages(@RequestParam(defaultValue = "3") int size) {
        return itemService.getTotalPages(size);
    }

    @GetMapping("/CardPaginated")
    public List<TraditionalItemDTO> getPaginatedItemsForCard(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        return itemService.getItemsForPage(page, size);
    }

    @GetMapping("/total-CardPages")
    public int getTotalCardPages(@RequestParam(defaultValue = "3") int size) {
        return itemService.getTotalPages(size);
    }

    @GetMapping("/searchItems/{keyword}")
    public ResponseEntity<ResponseDTO> searchItems(@PathVariable("keyword") String keyword) {
        List<TraditionalItemDTO> traditionalItems = itemService.searchItems(keyword);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(VarList.OK, "Search Success", traditionalItems));
    }


}
