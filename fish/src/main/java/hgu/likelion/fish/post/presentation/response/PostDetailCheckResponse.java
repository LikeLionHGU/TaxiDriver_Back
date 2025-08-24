package hgu.likelion.fish.post.presentation.response;

import hgu.likelion.fish.commons.image.entity.Image;
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
    private String failedReason;

    private List<String> images;
}
