package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import lk.ijse.gdse.traditionalexperiencebackend.repo.TraditionalItemRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.TraditionalItemService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TraditionalItemServiceImpl implements TraditionalItemService {

    @Autowired
    private TraditionalItemRepo itemRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int addItem(TraditionalItemDTO itemDTO) {
        if(itemRepo.existsByItemName(itemDTO.getItemName())) {
            return VarList.Not_Acceptable;
        }else {
            try {
                itemRepo.save(modelMapper.map(itemDTO, TraditionalItem.class));
                return VarList.Created;
            }catch (Exception e) {
                return VarList.Bad_Gateway;
            }
        }
    }

    @Override
    public List<TraditionalItemDTO> getAllItems() {
        List<TraditionalItem> traditionalItems = itemRepo.findAll();
        return traditionalItems.stream()
                .map(traditionalItem -> modelMapper.map(traditionalItem, TraditionalItemDTO.class))
                .toList();
    }

    @Override()
    public TraditionalItemDTO getItemById(Long id) {
        return itemRepo.findById(id)
                .map(item -> modelMapper.map(item, TraditionalItemDTO.class))
                .orElse(null);    }

    @Override
    public int updateItem(TraditionalItemDTO itemDTO) {
        try {

            TraditionalItem existingItem = itemRepo.findById(itemDTO.getId()).orElse(null);
            if (existingItem == null) {
                return VarList.Not_Found;
            }


            TraditionalItem itemWithSameName = itemRepo.findByItemName(itemDTO.getItemName());
            if(itemWithSameName != null && !itemWithSameName.getId().equals(itemDTO.getId())) {
                return VarList.Not_Acceptable;
            }


            existingItem.setItemName(itemDTO.getItemName());
            existingItem.setItemShortDescription(itemDTO.getItemShortDescription());
            existingItem.setItemDescription(itemDTO.getItemDescription());


            if(itemDTO.getItemImage() != null && !itemDTO.getItemImage().isEmpty()) {
                existingItem.setItemImage(itemDTO.getItemImage());
            }


            itemRepo.save(existingItem);
            return VarList.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return VarList.Bad_Gateway;
        }
    }


}
