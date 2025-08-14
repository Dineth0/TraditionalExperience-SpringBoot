package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import lk.ijse.gdse.traditionalexperiencebackend.repo.TraditionalItemRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.TraditionalItemService;
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
    public void addItem(TraditionalItemDTO itemDTO) {
        TraditionalItem entity = modelMapper.map(itemDTO, TraditionalItem.class);
        itemRepo.save(entity);
    }

    @Override
    public List<TraditionalItemDTO> getAllItems() {
        return itemRepo.findAll()
                .stream()
                .map(item -> modelMapper.map(item, TraditionalItemDTO.class))
                .toList();
    }

    @Override()
    public TraditionalItemDTO getItemById(UUID id) {
        return modelMapper.map(itemRepo.findById(id).orElseThrow(() -> new RuntimeException("Item Not Found")), TraditionalItemDTO.class);
    }


}
