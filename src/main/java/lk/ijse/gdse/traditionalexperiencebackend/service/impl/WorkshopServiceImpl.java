package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
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
    public int addWorkshop(WorkshopDTO workshopDTO) {
        try{
            workshopRepo.save(modelMapper.map(workshopDTO, Workshop.class));
            return VarList.Created;
        }catch(Exception e){
            return VarList.Bad_Gateway;
        }
    }
}
