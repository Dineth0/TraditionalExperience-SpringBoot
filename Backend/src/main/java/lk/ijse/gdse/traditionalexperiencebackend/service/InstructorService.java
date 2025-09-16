package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface InstructorService {
    int addInstructor(InstructorDTO instructorDTO);

    List<InstructorDTO> getAllInstructors();
    int updateInstructor(InstructorDTO instructorDTO);
    InstructorDTO getInstructorById(Long id);
    boolean deleteInstructor(Long id);
    Page<InstructorDTO> getAllInstructorsPage(Pageable pageable);
    List<InstructorDTO> searchInstructors(String keyword);
    public List<InstructorDTO> getInstructorForPage(int page, int size);
    public int getTotalPages(int size);




}
