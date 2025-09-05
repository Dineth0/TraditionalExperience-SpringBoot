package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.UserDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopRegistrationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.User;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import lk.ijse.gdse.traditionalexperiencebackend.entity.WorkshopRegistration;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRegistrationRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopRegistrationService;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkshopRegistrationServiceImpl implements WorkshopRegistrationService {

    @Autowired
    WorkshopRegistrationRepo workshopRegistrationRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    WorkshopService workshopService;

    @Override
    public int registerWorkshop(WorkshopRegistrationDTO workshopRegistrationDTO) {

            WorkshopRegistration workshopRegistration = modelMapper.map(workshopRegistrationDTO, WorkshopRegistration.class);

            if(workshopRegistrationDTO.getUserId() != null){
                User user = new User();
                user.setId(workshopRegistrationDTO.getUserId());
                workshopRegistration.setUser(user);
            }
            if(workshopRegistrationDTO.getWorkshopId() != null){
                Workshop workshop = new Workshop();
                workshop.setId(workshopRegistrationDTO.getWorkshopId());
                workshopRegistration.setWorkshop(workshop);
            }
            boolean exists = workshopRegistrationRepo.existsByWorkshopTimeAndSelectWorkshopDateAndWorkshopId(workshopRegistrationDTO.getWorkshopTime(),workshopRegistrationDTO.getSelectWorkshopDate(),workshopRegistrationDTO.getWorkshopId());

            if(exists){
                return VarList.Not_Acceptable;
            }else {
                try{
                    workshopRegistrationRepo.save(modelMapper.map(workshopRegistrationDTO, WorkshopRegistration.class)  );
                    return VarList.Created;
                }catch(Exception e){
                    return VarList.Bad_Gateway;
                }
            }
    }

    @Override
    public List<Map<String, Object>> checkAvailability(Long workshopId, Date date) {
        WorkshopDTO workshop = workshopService.getWorkshopById(workshopId);
        List<String> times = workshop.getTime();

        List<Map<String,Object>> result = new ArrayList<>();

        for(String t : times){
            boolean isBooked = workshopRegistrationRepo
                    .existsByWorkshopTimeAndSelectWorkshopDateAndWorkshopId(t, date, workshopId);

            Map<String,Object> slot = new HashMap<>();
            slot.put("time", t);
            slot.put("available", !isBooked);
            result.add(slot);
        }
        return result;
    }

    @Override
    public boolean cancelBooking(Long id) {
        if(workshopRegistrationRepo.existsById(id)){
            workshopRegistrationRepo.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<WorkshopRegistrationDTO> getBookingsByUserId(Long userId) {
        if(userId == null){
            return new ArrayList<>();
        }
        List<WorkshopRegistration> workshopRegistrations = workshopRegistrationRepo.findByUserId(userId);
        return workshopRegistrations.stream()
                .map(workshop -> modelMapper.map(workshop, WorkshopRegistrationDTO.class))
                .toList();
    }

    @Override
    public List<WorkshopRegistrationDTO> getAlRegistrations() {
        List<WorkshopRegistration> registrations = workshopRegistrationRepo.findAll();
        return registrations.stream()
                .map(reg -> {
                   WorkshopRegistrationDTO workshopRegistrationDTO = modelMapper.map(reg, WorkshopRegistrationDTO.class);
                    if(reg.getWorkshop() != null){
                        workshopRegistrationDTO.setWorkshopName(reg.getWorkshop().getTitle());
                    }
                    return workshopRegistrationDTO;
                }).toList();


    }


}
