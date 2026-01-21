package dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateTrainingDto {
    private Long athleteId;
    private Long sportId;
    private LocalDateTime date;
    private String metric;
}