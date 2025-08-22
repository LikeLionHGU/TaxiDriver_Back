package hgu.likelion.fish.post.presentation.response;

import hgu.likelion.fish.post.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCheckResponse {

    private int isLogin;
    private List<Post> posts;
}
