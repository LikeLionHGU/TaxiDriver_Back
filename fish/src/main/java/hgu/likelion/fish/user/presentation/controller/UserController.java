package hgu.likelion.fish.user.presentation.controller;

import hgu.likelion.fish.commons.jwt.MyPrincipal;
import hgu.likelion.fish.user.application.dto.UserDto;
import hgu.likelion.fish.user.application.service.UserService;
import hgu.likelion.fish.user.presentation.request.UserAdminSignRequest;
import hgu.likelion.fish.user.presentation.request.UserBuyerSignRequest;
import hgu.likelion.fish.user.presentation.request.UserSellerSignRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @PostMapping("/user/signin/admin")
    @CrossOrigin("*")
    public ResponseEntity<Boolean> signInAdmin(@AuthenticationPrincipal MyPrincipal principal, @RequestBody UserAdminSignRequest request) {
        String userId = principal.getUserId();

        Boolean result = userService.updateAdminUser(UserDto.toUpdateAdmin(request, userId));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/user/signin/buyer")
    @CrossOrigin("*")
    public ResponseEntity<Boolean> signInBuyer(@AuthenticationPrincipal MyPrincipal principal, @RequestBody UserBuyerSignRequest request) {
        String userId = principal.getUserId();

        Boolean result = userService.updateBuyerUser(UserDto.toUpdateBuyer(request, userId));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/user/signin/seller")
    @CrossOrigin("*")
    public ResponseEntity<Boolean> signInSeller(@AuthenticationPrincipal MyPrincipal principal, @RequestBody UserSellerSignRequest request) {
        String userId = principal.getUserId();

        Boolean result = userService.updateSellerUser(UserDto.toUpdateSeller(request, userId));

        return ResponseEntity.ok(result);
    }
}
