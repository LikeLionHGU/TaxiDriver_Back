package hgu.likelion.fish.user.presentation.controller;

import hgu.likelion.fish.commons.jwt.MyPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/test")
    @CrossOrigin("*")
    public ResponseEntity<Void> test() {
        System.out.println("test");



        return ResponseEntity.ok(null);
    }

    @PostMapping("/temp")
    @CrossOrigin("*")
    public ResponseEntity<Void> temp() {
        System.out.println("temp");



        return ResponseEntity.ok(null);
    }

    @PostMapping("/user/update")
    @CrossOrigin("*")
    public ResponseEntity<String> updateUserInformation(@AuthenticationPrincipal MyPrincipal principal) {
        return ResponseEntity.ok("현재 로그인한 사용자 ID = " + principal.getUserId());
    }

}
