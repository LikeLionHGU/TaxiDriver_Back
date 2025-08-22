package hgu.likelion.fish.post.presentation.response;

import lombok.*;

@Data
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostAddResponse {

    private int isLogin;
    private int isSuccess;
}
