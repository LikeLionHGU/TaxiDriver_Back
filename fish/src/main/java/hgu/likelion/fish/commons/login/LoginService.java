package hgu.likelion.fish.commons.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import hgu.likelion.fish.commons.jwt.JWTProvider;
import hgu.likelion.fish.commons.login.social.GoogleOAuth;
import hgu.likelion.fish.commons.login.social.GoogleOAuthToken;
import hgu.likelion.fish.commons.login.social.GoogleUser;
import hgu.likelion.fish.commons.security.Authority;
import hgu.likelion.fish.user.domain.entity.User;
import hgu.likelion.fish.user.domain.repository.UserRepository;
import hgu.likelion.fish.user.presentation.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final GoogleOAuth googleOAuth;
    private final HttpServletResponse response;
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    public void request() throws IOException {
        String redirectURL = googleOAuth.getOauthRedirectURL();

        response.sendRedirect(redirectURL);
    }

    @Transactional
    public GoogleUser googleLogin(String code) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(code);
        //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
        GoogleOAuthToken oAuthToken = googleOAuth.getAccessToken(accessTokenResponse);

        //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(oAuthToken);
        //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
        GoogleUser googleUser = googleOAuth.getUserInfo(userInfoResponse);

        return googleUser;
    }

    @Transactional
    public UserResponse login(GoogleUser googleUser) {
        Optional<User> optionalUser = userRepository.findById(googleUser.getId());
        User user;

        if(optionalUser.isEmpty()) {
            user = User.builder()
                    .id(googleUser.getId())
                    .name(googleUser.getName())
                    .email(googleUser.getEmail())
                    .build();
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        return UserResponse.toResponse(user, jwtProvider.createToken(user.getId(), user.getRoles()));
    }
}
