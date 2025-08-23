package hgu.likelion.fish.post.presentation.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostAuctionListResponse {

    private Long totalCount;
    private Long readyCount;
    private Long currentCount;
    private Long finishCount;

    public static PostAuctionListResponse toResponse(Long totalCount, Long readyCount, Long currentCount, Long finishCount) {
        return PostAuctionListResponse.builder()
                .totalCount(totalCount)
                .readyCount(readyCount)
                .currentCount(currentCount)
                .finishCount(finishCount)
                .build();
    }
}
