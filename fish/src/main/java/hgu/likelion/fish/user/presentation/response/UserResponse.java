package hgu.likelion.fish.user.presentation.response;

import hgu.likelion.fish.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String userId;
    private String email;
    private String token;


    public static UserResponse toResponse(User user, String token) {
        return UserResponse.builder()
                .name(user.getName())
                .userId(user.getId())
                .email(user.getEmail())
                .token(token)
                .build();
    }

    /**
     * 해당 코드는 HSF 테스트 용입니다.
     * @param user
     * @return
     */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .build();
    }
}