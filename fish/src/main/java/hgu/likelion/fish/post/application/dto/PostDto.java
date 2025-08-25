package hgu.likelion.fish.post.application.dto;

import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.post.presentation.request.PostInfoRequest;
import hgu.likelion.fish.user.application.dto.UserDto;
import hgu.likelion.fish.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Long id;
    private String name;
    private Integer fishCount;
    private String fishWeight;

    private String fishStatus;
    private String salesMethod;
    private Integer reservePrice;
    private RegisterStatus registrationStatus;
    private AuctionStatus auctionStatus;
    private String commentBySeller;
    private String aiEvaluation;
    private LocalDateTime registeredDate;
    private LocalDateTime triggerAt;
    private LocalDateTime startedAt;



    private UserDto seller;


    private String origin;
    private List<String> urls;

    private Integer topPrice;


    public static PostDto from(Post post, List<String> urls) {
        return PostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .fishCount(post.getFishCount())
                .fishWeight(post.getFishWeight())
                .fishStatus(post.getFishStatus())
                .salesMethod(post.getSalesMethod())
                .reservePrice(post.getReservePrice())
                .seller(UserDto.toPostGetResponse(post.getSeller()))
                .urls(urls)
                .build();
    }

    public static PostDto fromInfoAdd(PostInfoRequest postInfoRequest) {
        return PostDto.builder()
                .name(postInfoRequest.getName())
                .fishCount(postInfoRequest.getFishCount())
                .fishWeight(postInfoRequest.getFishWeight())
                .fishStatus(postInfoRequest.getFishStatus())
                .salesMethod(postInfoRequest.getSalesMethod())
                .reservePrice(postInfoRequest.getReservePrice())
                .build();
    }

    public static PostDto toGetResponse(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .seller(UserDto.toPostGetResponse(post.getSeller()))
                .registrationStatus(post.getRegistrationStatus())
                .fishCount(post.getFishCount())
                .fishWeight(post.getFishWeight())
                .registeredDate(post.getRegDate())
                .build();
    }

    public static PostDto from(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .seller(UserDto.toPostGetResponse(post.getSeller()))
                .registrationStatus(post.getRegistrationStatus())
                .fishCount(post.getFishCount())
                .fishWeight(post.getFishWeight())
                .registrationStatus(post.getRegistrationStatus())
                .reservePrice(post.getReservePrice())
                .aiEvaluation(post.getAiEvaluation())
                .build();
    }

    public static PostDto toAuctionResponse(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .reservePrice(post.getReservePrice())
                .fishCount(post.getFishCount())
                .fishStatus(post.getFishStatus())
                .fishWeight(post.getFishWeight())
                .triggerAt(post.getTriggerAt())
                .reservePrice(post.getReservePrice())
                .salesMethod(post.getSalesMethod())
                .seller(UserDto.toPostGetResponse(post.getSeller()))
                .auctionStatus(post.getAuctionStatus())
                .registeredDate(post.getRegDate())
                .startedAt(post.getStartedAt())
                .build();
    }

    public static PostDto toGetOneResponse(Post post, List<String> urls, Integer topPrice) {
        return PostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .urls(urls)
                .auctionStatus(post.getAuctionStatus())
                .origin(post.getOrigin())
                .seller(UserDto.toPostGetResponse(post.getSeller()))
                .registeredDate(post.getRegDate())
                .fishCount(post.getFishCount())
                .fishWeight(post.getFishWeight())
                .salesMethod(post.getSalesMethod())
                .triggerAt(post.getTriggerAt())
                .topPrice(topPrice)
                .reservePrice(post.getReservePrice())
                .build();
    }

}
