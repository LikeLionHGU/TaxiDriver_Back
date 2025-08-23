package hgu.likelion.fish.post.presentation.response;


import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.post.application.dto.PostDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostGetResponse {
    private Long id;
    private String name;
    private String sellerName;
    private RegisterStatus registrationStatus;
    private Integer fishCount;
    private String fishWeight;
    private LocalDateTime registeredDate;


    public static PostGetResponse toResponse(PostDto postDto) {
        return PostGetResponse.builder()
                .id(postDto.getId())
                .name(postDto.getName())
                .sellerName(postDto.getSeller().getName())
                .registrationStatus(postDto.getRegistrationStatus())
                .fishCount(postDto.getFishCount())
                .fishWeight(postDto.getFishWeight())
                .registeredDate(postDto.getRegisteredDate())
                .build();
    }
}
