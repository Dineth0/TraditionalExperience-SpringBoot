package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    @Autowired
    WorkshopRepo workshopRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void addWorkshop(WorkshopDTO workshopDTO) {
        workshopRepo.save(modelMapper.map(workshopDTO, Workshop.class));
    }
}
