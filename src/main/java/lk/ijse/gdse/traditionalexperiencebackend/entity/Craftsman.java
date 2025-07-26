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
public class Craftsman {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String craftsmanName;
    private int age;
    private String craftsmanEmail;
    private String craftsmanPhone;
    private String image;

    @OneToMany(mappedBy = "craftsman", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workshop> workshop;
}
