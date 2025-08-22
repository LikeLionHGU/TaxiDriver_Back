package hgu.likelion.fish.post.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostInfoRequest {

    private String name;
    private String fishStatus;
    private String salesMethod;
    private int fishCount;
    private String fishWeight;
    private int reservePrice;
}
