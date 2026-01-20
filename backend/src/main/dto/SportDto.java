package dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SportDto {
    private Long id;
    private String name;
    private String unit;
}