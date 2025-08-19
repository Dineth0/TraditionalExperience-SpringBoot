package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.ResponseDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    @Autowired
    WorkshopRepo workshopRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public int addWorkshop(WorkshopDTO workshopDTO) {
        try{
           Workshop workshop = modelMapper.map(workshopDTO, Workshop.class);

           if(workshopDTO.getItemId() != null ){
               TraditionalItem traditionalItem = new TraditionalItem();
               traditionalItem.setId(workshopDTO.getItemId());
               workshop.setItem(traditionalItem);
           }
           if(workshopDTO.getInstructorId() != null){
               Instructor instructor = new Instructor();
               instructor.setId(workshopDTO.getInstructorId());
               workshop.setInstructor(instructor);
           }
           workshopRepo.save(workshop);
           return VarList.Created;
        }catch(Exception e){
            return VarList.Bad_Gateway;
        }

    }
    @Override
    public List<WorkshopDTO> getAllWorkshops() {
        List<Workshop> workshops = workshopRepo.findAll();
        return workshops.stream()
                .map(workshop -> modelMapper.map(workshop, WorkshopDTO.class))
                .toList();
    }

    @Override
    public List<WorkshopDTO> getWorkshopsByItem(Long itemId) {
        List<Workshop> workshops = workshopRepo.findByItemId(itemId);
        return workshops.stream()
                .map(workshop -> modelMapper.map(workshop, WorkshopDTO.class))
                .toList();
    }
}
