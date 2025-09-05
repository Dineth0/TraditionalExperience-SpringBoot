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

    @Override
    public WorkshopDTO getWorkshopById(Long id) {
        return workshopRepo.findById(id)
                .map(workshop -> modelMapper.map(workshop, WorkshopDTO.class))
                .orElse(null);
    }

    @Override
    public int getParticipantsById(Long id) {
        Integer count = workshopRepo.getParticipantCountById(id);
        return (count != null) ? count : 0;
    }

    @Override
    public int updateWorkshop(WorkshopDTO workshopDTO) {
        try{
            Workshop existingWorkshop = workshopRepo.findById(workshopDTO.getId()).orElse(null);

            if(existingWorkshop == null){
                return VarList.Not_Found;
            }
            Workshop newWorkshop =workshopRepo.findByTitle(workshopDTO.getTitle());
            if(newWorkshop != null && !newWorkshop.getId().equals(workshopDTO.getId())){
                return VarList.Not_Acceptable;
            }
            existingWorkshop.setTitle(workshopDTO.getTitle());
            existingWorkshop.setDescription(workshopDTO.getDescription());
            existingWorkshop.setDuration(workshopDTO.getDuration());
            existingWorkshop.setLanguage(workshopDTO.getLanguage());
            existingWorkshop.setParticipantCount(workshopDTO.getParticipantCount());
            existingWorkshop.setFee(workshopDTO.getFee());
            existingWorkshop.setAddress(workshopDTO.getAddress());
            existingWorkshop.setTime(workshopDTO.getTime());
            existingWorkshop.setInclude(workshopDTO.getInclude());

            if(workshopDTO.getImage() != null && !workshopDTO.getImage().isEmpty()){
                existingWorkshop.setImage(workshopDTO.getImage());
            }
            workshopRepo.save(existingWorkshop);
            return VarList.Updated;
         }catch(Exception e){
            System.out.println(e.getMessage());
            return VarList.Bad_Gateway;
        }
    }

    @Override
    public boolean deleteWorkshop(Long id) {
        if(workshopRepo.existsById(id)){
            workshopRepo.deleteById(id);
            return true;
        }else {
            return false;
        }
    }


}
