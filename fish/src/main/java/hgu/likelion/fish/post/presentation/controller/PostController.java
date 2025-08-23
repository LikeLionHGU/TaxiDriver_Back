package hgu.likelion.fish.post.presentation.controller;

import hgu.likelion.fish.commons.image.service.S3Service;
import hgu.likelion.fish.commons.jwt.MyPrincipal;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.application.service.PostService;
import hgu.likelion.fish.post.presentation.request.PostInfoRequest;
import hgu.likelion.fish.post.presentation.response.PostAddResponse;
import hgu.likelion.fish.post.presentation.response.PostCheckResponse;
import hgu.likelion.fish.user.application.service.UserService;
import hgu.likelion.fish.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final S3Service s3Service;

    @PostMapping("/add")
    public ResponseEntity<PostAddResponse> addPost(
            @AuthenticationPrincipal MyPrincipal principal,
            @RequestParam(value = "image", required = false) MultipartFile[] images,
            @RequestPart("post")PostInfoRequest info) {
        PostAddResponse response = new PostAddResponse();

        try{
            String userId = (String) principal.getUserId();
            if (userId == null) {
                response.setIsLogin(0);
                response.setIsSuccess(0);
                return ResponseEntity.ok(response);
            }

            User user = userService.findUserById(userId);
            response.setIsLogin(loginOrNot(principal));

            PostDto postDto = postService.savePost(PostDto.fromInfoAdd(info), images, "va/", user);
            if (postDto == null) {
                response.setIsSuccess(0);
            } else {
                response.setIsSuccess(1);
            }
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            response.setIsSuccess(0);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/check/status")
    public ResponseEntity<PostCheckResponse> getPostCheck(
            @AuthenticationPrincipal MyPrincipal principal,
            @RequestParam String status) {
        PostCheckResponse response = new PostCheckResponse();

        try{
            response.setIsLogin(loginOrNot(principal));

            List<PostDto> postDtoList = postService.getPostCheck(status);
            response.setPostDtoList(postDtoList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setPostDtoList(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/check/all")
    public ResponseEntity<PostCheckResponse> getAllPostCheck(
            @AuthenticationPrincipal MyPrincipal principal) {
        PostCheckResponse response = new PostCheckResponse();

        try{
            response.setIsLogin(loginOrNot(principal));

            List<PostDto> postDtoList = postService.getAllPostCheck();
            response.setPostDtoList(postDtoList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setPostDtoList(null);
            return ResponseEntity.ok(response);
        }
    }

    private int loginOrNot(@AuthenticationPrincipal MyPrincipal principal) {
        String userId = (String) principal.getUserId();
        if (userId == null) {
            return 0;
        }
        return 1;
    }
}
