package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;

import java.util.List;

public interface InstructorService {
    int addInstructor(InstructorDTO instructorDTO);

    List<InstructorDTO> getAllInstructors();
}
