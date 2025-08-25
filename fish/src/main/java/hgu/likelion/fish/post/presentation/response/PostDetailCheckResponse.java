package hgu.likelion.fish.post.presentation.response;

import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.commons.image.entity.Image;
import hgu.likelion.fish.post.domain.entity.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailCheckResponse {

    private String name;
    private String salesMethod;
    private LocalDateTime registeredDate;
    private String origin;
    private String aiEvaluation;
    private String reason;
    private Integer reservedPrice;
    private RegisterStatus registerStatus;
    private String rejectReason;

    private List<String> images;


    public static PostDetailCheckResponse toResponse(Post post, List<String> images) {
        return PostDetailCheckResponse.builder()
                .name(post.getName())
                .rejectReason(post.getFailedReason())
                .registerStatus(post.getRegistrationStatus())
                .salesMethod(post.getSalesMethod())
                .reservedPrice(post.getReservePrice())
                .registeredDate(post.getRegDate())
                .origin(post.getOrigin())
                .aiEvaluation(post.getAiEvaluation())
                .reason(post.getReason())
                .images(images)
                .build();
    }
}
