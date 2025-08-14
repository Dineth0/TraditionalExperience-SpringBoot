package lk.ijse.gdse.traditionalexperiencebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
public class TraditionalItem {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String itemName;
    private String itemShortDescription;
    @Column(length = 10000)
    private String itemDescription;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> itemImage;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workshop> workshop;
}
