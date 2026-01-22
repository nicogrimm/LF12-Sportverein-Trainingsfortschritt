package sportverein.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sport_id")
    private int sportId;
    
    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;
    
    @Column(name = "unit", nullable = false, length = 16)
    private String unit;
}