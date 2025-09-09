package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.gdse.traditionalexperiencebackend.dto.UserDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopRegistrationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.User;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import lk.ijse.gdse.traditionalexperiencebackend.entity.WorkshopRegistration;
import lk.ijse.gdse.traditionalexperiencebackend.repo.PaymentRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.WorkshopRegistrationRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopRegistrationService;
import lk.ijse.gdse.traditionalexperiencebackend.service.WorkshopService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkshopRegistrationServiceImpl implements WorkshopRegistrationService {


   private final WorkshopRegistrationRepo workshopRegistrationRepo;
   private final PaymentRepo paymentRepo;
   private final ModelMapper modelMapper;
   private final WorkshopService workshopService;

    @Override
    public WorkshopRegistrationDTO registerWorkshop(WorkshopRegistrationDTO workshopRegistrationDTO) {

            WorkshopRegistration workshopRegistration = modelMapper.map(workshopRegistrationDTO, WorkshopRegistration.class);

            workshopRegistration.setPaymentStatus("Pending");
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
                return null;
            }else {
              WorkshopRegistration saved = workshopRegistrationRepo.save(workshopRegistration);
              return modelMapper.map(saved, WorkshopRegistrationDTO.class);
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
    @Transactional
    public boolean cancelBooking(Long id) {
        if(workshopRegistrationRepo.existsById(id)){
//            paymentRepo.deleteById(id);
       WorkshopRegistration workshopRegistration = workshopRegistrationRepo.findById(id)
               .orElseThrow(() -> new RuntimeException("Not found"));
       workshopRegistration.getPayments().clear();
       workshopRegistrationRepo.delete(workshopRegistration);
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
