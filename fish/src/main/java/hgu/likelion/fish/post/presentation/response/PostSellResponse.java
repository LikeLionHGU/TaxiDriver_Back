package hgu.likelion.fish.post.presentation.response;


import hgu.likelion.fish.post.domain.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSellResponse {

    private String name;
    private Integer count;
    private Integer price;
    private LocalDateTime date;

    public static PostSellResponse toResponse(Post post) {
        return PostSellResponse.builder()
                .name(post.getName())
                .count(post.getFishCount())
                .price(post.getTotalPrice())
                .date(post.getTriggerAt())
                .build();
    }
}
