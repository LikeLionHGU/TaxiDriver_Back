package hgu.likelion.fish.commons.login;

import hgu.likelion.fish.commons.login.social.GoogleUser;
import hgu.likelion.fish.user.presentation.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @CrossOrigin("*")
    @PostMapping("/login/oauth2/google")
    public void socialLoginRedirect() throws IOException {
        loginService.request();
    }

    @ResponseBody
    @CrossOrigin("*")
    @GetMapping(value = "/api/v1/oauth2/google") // 여기서 로그인 및 회원가입 여부 확인을 해야함
    public void callback (@RequestParam(value = "code") String code, HttpServletResponse response) throws IOException {


        GoogleUser googleResponse = loginService.googleLogin(code);

        UserResponse userResponse = loginService.login(googleResponse);

        String userId = URLEncoder.encode(userResponse.getUserId(), StandardCharsets.UTF_8.toString());
        String userName = URLEncoder.encode(userResponse.getName(), StandardCharsets.UTF_8.toString());
        String token = URLEncoder.encode(userResponse.getToken(), StandardCharsets.UTF_8.toString());
        System.out.println(token);


        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000")
                .queryParam("userId", userId)
                .queryParam("userName", userName)
                .queryParam("token", token)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }


}
