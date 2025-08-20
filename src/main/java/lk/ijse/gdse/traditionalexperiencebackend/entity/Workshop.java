package lk.ijse.gdse.traditionalexperiencebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Workshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private String duration;
    private String language;
    private int participantCount;
    private String fee;
    private String address;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> image;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> time;
//    private String mapUrl;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private TraditionalItem item;

    @ManyToOne
    @JoinColumn(name = "instructorId")
    private Instructor instructor;

    @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> review;

    @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkshopRegistration> workshopRegistration;
}
