package sportverein.dto;

import java.time.OffsetDateTime;

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
    private int id;
    private int athleteId;
    private int sportId;
    private OffsetDateTime date;
    private Float metric;
}