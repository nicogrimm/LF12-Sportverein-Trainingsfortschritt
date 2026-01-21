package sportverein.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class TrainingDto {
    private Long id;
    private Long athleteId;
    private Long sportId;
    private LocalDateTime date;
    private String metric;
}