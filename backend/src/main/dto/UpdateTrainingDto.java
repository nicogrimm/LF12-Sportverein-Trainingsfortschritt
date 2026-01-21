package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateTrainingDto {
    private Long athleteId;
    private Long sportId;
    private LocalDateTime date;
    private String metric;
}