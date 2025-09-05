package lk.ijse.gdse.traditionalexperiencebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String title;
    private String description;
    private Date wentDate;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> image;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
     @ManyToOne
    @JoinColumn(name = "workshopId")
    private Workshop workshop;



}
