package hgu.likelion.fish.commons.login;

import hgu.likelion.fish.commons.login.social.GoogleUser;
import hgu.likelion.fish.user.presentation.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @CrossOrigin("*")
    @GetMapping("/login/oauth2/google")
    public void socialLoginRedirect() throws IOException {
        loginService.request();
    }

    @ResponseBody
    @CrossOrigin("*")
    @GetMapping(value = "/api/v1/oauth2/google") // 여기서 로그인 및 회원가입 여부 확인을 해야함
    public void callback (@RequestParam(value = "code") String code, HttpServletResponse response) throws IOException {
        GoogleUser googleResponse = loginService.googleLogin(code);
        UserResponse userResponse = loginService.login(googleResponse);

        String token = URLEncoder.encode(userResponse.getToken(), StandardCharsets.UTF_8.toString());

        ResponseCookie cookie = ResponseCookie.from("SESSION", token)
                .httpOnly(true).secure(true).sameSite("Lax")
                .path("/").maxAge(Duration.ofMinutes(30))
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        response.sendRedirect("http://localhost:3000/temp"); // 파라미터 없음
        System.out.println(token);

    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res) {
        var session = req.getSession(false);
        if (session != null) session.invalidate(); // Redis/Spring Session도 정리
        // 2) 브라우저 쿠키 만료
        ResponseCookie expired = ResponseCookie.from("JSESSIONID", "")
                .httpOnly(true).secure(true).sameSite("Lax")
                .path("/").maxAge(0).build();
        res.addHeader("Set-Cookie", expired.toString());
        return ResponseEntity.noContent().build();
    }


}
