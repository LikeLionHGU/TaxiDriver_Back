package hgu.likelion.fish.post.presentation.response;

import lombok.*;

@Data
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostAddResponse {

    private Integer isLogin;
    private Integer isSuccess;
}
