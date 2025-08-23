package hgu.likelion.fish.price.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    private Long id;
    private String name;
    private Integer count;
    private Long average;
    private Long highestPrice;
    private Long lowestPrice;
    private LocalDateTime date;
}
