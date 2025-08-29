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

    @Override
    public int updateInstructor(InstructorDTO instructorDTO) {
        try{
            Instructor existingInstructor = instructorRepo.findById(instructorDTO.getId()).orElse(null);
            if(existingInstructor == null){
                return VarList.Not_Found;
            }
            Instructor newInstructor =instructorRepo.findByInstructorName(instructorDTO.getInstructorName());
            if(newInstructor != null && !newInstructor.getId().equals(instructorDTO.getId())){
                return VarList.Not_Acceptable;
            }
            existingInstructor.setInstructorName(instructorDTO.getInstructorName());
            existingInstructor.setAge(instructorDTO.getAge());
            existingInstructor.setCategory(instructorDTO.getCategory());
            existingInstructor.setInstructorEmail(instructorDTO.getInstructorEmail());
            existingInstructor.setInstructorPhone(instructorDTO.getInstructorPhone());

            if(instructorDTO.getImage() != null && !instructorDTO.getImage().isEmpty()){
                existingInstructor.setImage(instructorDTO.getImage());
            }
            instructorRepo.save(existingInstructor);
            return VarList.Updated;
        }catch (Exception e){
            return VarList.Bad_Gateway;
        }
    }

    @Override
    public InstructorDTO getInstructorById(Long id) {
        return instructorRepo.findById(id)
                .map(instructor -> modelMapper.map(instructor, InstructorDTO.class))
                .orElse(null);
    }
}
