package hgu.likelion.fish.post.presentation.response;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostAuctionCountResponse {

    private Long totalCount;
    private Long waitCount;
    private Long finishCount;

    public static PostAuctionCountResponse toResponse(Long totalCount, Long waitCount, Long finishCount) {
        return PostAuctionCountResponse.builder()
                .totalCount(totalCount)
                .waitCount(waitCount)
                .finishCount(finishCount)
                .build();
    }
}
