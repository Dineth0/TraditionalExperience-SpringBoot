package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import lk.ijse.gdse.traditionalexperiencebackend.repo.InstructorRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.InstructorService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    InstructorRepo instructorRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public int addInstructor(InstructorDTO instructorDTO) {
        if(instructorRepo.existsByInstructorName(instructorDTO.getInstructorName())){
        return VarList.Not_Acceptable;
         }else {
            try{
                instructorRepo.save(modelMapper.map(instructorDTO, Instructor.class));
                return VarList.Created;
            }catch (Exception e){
                return VarList.Bad_Gateway;
            }
        }

    }

    @Override
    public List<InstructorDTO> getAllInstructors() {
        List<Instructor> instructors = instructorRepo.findAll();
        return instructors.stream()
                .map(instructor -> modelMapper.map(instructor, InstructorDTO.class))
                .toList();
    }
}
