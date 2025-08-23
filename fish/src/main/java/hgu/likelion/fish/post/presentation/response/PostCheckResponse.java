package hgu.likelion.fish.post.presentation.response;

import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCheckResponse {

    private Long id;
    private String name;
    private String sellerName;
    private RegisterStatus registerStatus;
    private Integer fishCount;
    private String fishWeight;
    private Integer reservePrice;
    private String aiEvaluation;

    public static PostCheckResponse from(PostDto postDto) {
        return PostCheckResponse.builder()
                .id(postDto.getId())
                .name(postDto.getName())
                .sellerName(postDto.getSeller().getName())
                .registerStatus(postDto.getRegistrationStatus())
                .fishCount(postDto.getFishCount())
                .fishWeight(postDto.getFishWeight())
                .reservePrice(postDto.getReservePrice())
                .aiEvaluation(postDto.getAiEvaluation())
                .build();
    }
}
