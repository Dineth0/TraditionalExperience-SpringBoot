package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import lk.ijse.gdse.traditionalexperiencebackend.repo.InstructorRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.InstructorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    InstructorRepo instructorRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void addInstructor(InstructorDTO instructorDTO) {
        instructorRepo.save(modelMapper.map(instructorDTO, Instructor.class));
    }
}
