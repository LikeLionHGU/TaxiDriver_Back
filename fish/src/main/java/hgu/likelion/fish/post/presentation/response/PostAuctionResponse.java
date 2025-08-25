package hgu.likelion.fish.post.presentation.response;

import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.user.application.dto.UserDto;
import lombok.*;

import java.time.Duration;
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
    private LocalDateTime triggerAt;
    private LocalDateTime startedAt;
    private UserDto seller;



    private Long remainTime;


    public static PostAuctionResponse toAuctionResponse(PostDto postDto) {
        Long secondsLeft = null;

        if(postDto.getAuctionStatus() == AuctionStatus.AUCTION_CURRENT) {
            // 종료 시간 = 시작 시간 + 10분
            LocalDateTime endAt = postDto.getStartedAt().plusMinutes(5);

            // 현재 시각
            LocalDateTime now = LocalDateTime.now();

            // 남은 초 계산 (음수가 될 수도 있음)
            secondsLeft = Duration.between(now, endAt).getSeconds();

            if(postDto.getAuctionStatus() != AuctionStatus.AUCTION_CURRENT) {
                secondsLeft = null;
            }
        }


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
                .remainTime(secondsLeft)
                .auctionStatus(postDto.getAuctionStatus())
                .registeredDate(postDto.getRegisteredDate())
                .triggerAt(postDto.getTriggerAt())
                .startedAt(postDto.getStartedAt())
                .build();
    }
}
