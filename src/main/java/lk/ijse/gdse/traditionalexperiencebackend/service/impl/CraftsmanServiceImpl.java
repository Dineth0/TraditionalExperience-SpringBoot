package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.CraftsmanDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Craftsman;
import lk.ijse.gdse.traditionalexperiencebackend.repo.CraftsmanRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.CraftsmanService;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CraftsmanServiceImpl implements CraftsmanService {

    @Autowired
    CraftsmanRepo craftsmanRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void addCraftsman(CraftsmanDTO craftsmanDTO) {
        craftsmanRepo.save(modelMapper.map(craftsmanDTO, Craftsman.class));
    }
}
