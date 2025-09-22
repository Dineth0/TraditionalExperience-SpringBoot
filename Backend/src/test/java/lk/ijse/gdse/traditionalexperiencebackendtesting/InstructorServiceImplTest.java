package lk.ijse.gdse.traditionalexperiencebackendtesting;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import lk.ijse.gdse.traditionalexperiencebackend.repo.InstructorRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.InstructorService;
import lk.ijse.gdse.traditionalexperiencebackend.service.impl.InstructorServiceImpl;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceImplTest {

    @Mock
    private InstructorRepo instructorRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InstructorServiceImpl instructorService;

    private Instructor instructor;
    private InstructorDTO instructorDTO;

    @BeforeEach
    void setUp() {
        instructor = Instructor.builder()
                .id(1L)
                .instructorName("Supun")
                .age(30)
                .category("Mask Carving")
                .instructorEmail("supun@example.com")
                .instructorPhone("0771234567")
                .image("image.png")
                .build();

        instructorDTO = new InstructorDTO();
        instructorDTO.setId(1L);
        instructorDTO.setInstructorName("Supun");
        instructorDTO.setAge(30);
        instructorDTO.setCategory("Mask Carving");
        instructorDTO.setInstructorEmail("supun@example.com");
        instructorDTO.setInstructorPhone("0771234567");
        instructorDTO.setImage("image.png");
    }
    @Test
    void shouldAddInstructor_WhenNameNotExists() {
        when(instructorRepo.existsByInstructorName("Supun")).thenReturn(false);
        when(modelMapper.map(instructorDTO, Instructor.class)).thenReturn(instructor);

        int result = instructorService.addInstructor(instructorDTO);

        assertThat(result).isEqualTo(VarList.Created);
        verify(instructorRepo, times(1)).save(instructor);
    }

    @Test
    void shouldNotAddInstructor_WhenNameExists() {
        when(instructorRepo.existsByInstructorName("Supun")).thenReturn(true);

        int result = instructorService.addInstructor(instructorDTO);

        assertThat(result).isEqualTo(VarList.Not_Acceptable);
        verify(instructorRepo, never()).save(any());
    }

    @Test
    void shouldGetInstructorById_WhenFound() {
        when(instructorRepo.findById(1L)).thenReturn(Optional.of(instructor));
        when(modelMapper.map(instructor, InstructorDTO.class)).thenReturn(instructorDTO);

        InstructorDTO result = instructorService.getInstructorById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getInstructorName()).isEqualTo("Supun");
    }

    @Test
    void shouldReturnNull_WhenInstructorNotFound() {
        when(instructorRepo.findById(1L)).thenReturn(Optional.empty());

        InstructorDTO result = instructorService.getInstructorById(1L);

        assertThat(result).isNull();
    }

    @Test
    void shouldDeleteInstructor_WhenExists() {
        when(instructorRepo.existsById(1L)).thenReturn(true);

        boolean result = instructorService.deleteInstructor(1L);

        assertThat(result).isTrue();
        verify(instructorRepo, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotDeleteInstructor_WhenNotExists() {
        when(instructorRepo.existsById(1L)).thenReturn(false);

        boolean result = instructorService.deleteInstructor(1L);

        assertThat(result).isFalse();
        verify(instructorRepo, never()).deleteById(any());
    }

    private CrudRepository<Object, Object> verify(InstructorRepo instructorRepo, VerificationMode never) {
        return null;
    }
}
