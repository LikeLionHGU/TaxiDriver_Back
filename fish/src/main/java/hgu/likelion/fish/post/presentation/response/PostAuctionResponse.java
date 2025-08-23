package hgu.likelion.fish.post.presentation.response;

import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.user.application.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostAuctionResponse {
    private Long id;
    private String name;
    private Integer fishCount;
    private String fishWeight;
    private String fishStatus;
    private String salesMethod;
    private Integer reservePrice;
    private AuctionStatus auctionStatus;
    private LocalDateTime registeredDate;
    private LocalDateTime startedAt;
    private UserDto seller;


    public static PostAuctionResponse toAuctionResponse(PostDto postDto) {
        return PostAuctionResponse.builder()
                .id(postDto.getId())
                .name(postDto.getName())
                .reservePrice(postDto.getReservePrice())
                .fishCount(postDto.getFishCount())
                .fishWeight(postDto.getFishWeight())
                .fishStatus(postDto.getFishStatus())
                .reservePrice(postDto.getReservePrice())
                .salesMethod(postDto.getSalesMethod())
                .seller(postDto.getSeller())
                .auctionStatus(postDto.getAuctionStatus())
                .registeredDate(postDto.getRegisteredDate())
                .startedAt(postDto.getStartedAt())
                .build();
    }
}
