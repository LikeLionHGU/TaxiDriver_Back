package hgu.likelion.fish.post.presentation.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {
    private Long totalCount;
    private Long readyCount;
    private Long approvedCount;
    private Long rejectedCount;

    public static PostListResponse toResponse(Long totalCount, Long readyCount, Long approvedCount, Long rejectedCount) {
        return PostListResponse.builder()
                .totalCount(totalCount)
                .readyCount(readyCount)
                .approvedCount(approvedCount)
                .rejectedCount(rejectedCount)
                .build();
    }
}
