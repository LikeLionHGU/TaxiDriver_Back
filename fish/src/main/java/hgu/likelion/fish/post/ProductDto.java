package hgu.likelion.fish.post;

import java.time.LocalDateTime;

// DTO
public record ProductDto(
        LocalDateTime createdAt,
        String name,
        String location,
        String size,
        String packaging,
        Long count,
        Long highestPrice,
        Long lowestPrice,
        Long averagePrice
) {}
