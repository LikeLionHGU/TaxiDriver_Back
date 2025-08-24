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
public class PostResponse {

    private Long id;
    private String name;
    private Integer fishCount;
    private String fishWeight;
    private String salesMethod;
    private Integer reservePrice;
    private LocalDateTime registeredDate;
    private LocalDateTime triggerAt;
    private UserDto seller;
    private String origin;
    private AuctionStatus auctionStatus;
    private List<String> urls;

    public static PostResponse toGetOneResponse(PostDto post) {
        return PostResponse.builder()
                .id(post.getId())
                .name(post.getName())
                .origin(post.getOrigin())
                .seller(post.getSeller())
                .registeredDate(post.getRegisteredDate())
                .auctionStatus(post.getAuctionStatus())
                .fishCount(post.getFishCount())
                .fishWeight(post.getFishWeight())
                .salesMethod(post.getSalesMethod())
                .triggerAt(post.getTriggerAt())
                .reservePrice(post.getReservePrice())
                .urls(post.getUrls())
                .build();
    }
}



