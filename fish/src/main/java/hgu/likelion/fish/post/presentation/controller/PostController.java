package hgu.likelion.fish.post.presentation.controller;

import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.DateStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.commons.image.service.S3Service;
import hgu.likelion.fish.commons.jwt.MyPrincipal;
import hgu.likelion.fish.post.ProductDto;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.application.service.PostService;
import hgu.likelion.fish.post.presentation.request.PostInfoRequest;
import hgu.likelion.fish.post.presentation.response.*;
import hgu.likelion.fish.user.application.service.UserService;
import hgu.likelion.fish.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
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
            String userId = principal.getUserId();

            User user = userService.findUserById(userId);
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

    @GetMapping("/get/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(PostResponse.toGetOneResponse(postService.getPost(id)));
    }


    // 전체 목록 가져옴
    // user 반영
    @GetMapping("/get/list")
    public ResponseEntity<PostListResponse> getListPosts(@AuthenticationPrincipal MyPrincipal principal) {
        String userId = principal.getUserId();

        return ResponseEntity.ok(postService.getPostListNumber(userId));
    }

    // user 반영
    @GetMapping("/get/auction/list")
    public ResponseEntity<PostAuctionListResponse> getAuctionPostCount(@AuthenticationPrincipal MyPrincipal principal) {

        String userId = principal.getUserId();
        return ResponseEntity.ok(postService.getPostAuctionListNumber(userId));
    }

    @GetMapping("/get/auction/list/admin")
    public ResponseEntity<PostAuctionListResponse> getAuctionPostCountAdmin() {
        return ResponseEntity.ok(postService.getPostAuctionListNumber());
    }






    // user 반영
    @GetMapping("/get/all/{value}")
    public ResponseEntity<List<PostGetResponse>> getAllPosts(@PathVariable DateStatus value, @AuthenticationPrincipal MyPrincipal principal) {
        return ResponseEntity.ok(postService.getAllPostWithUser(value, principal.getUserId()).stream().map(PostGetResponse::toResponse).toList());
    }


    // user 반영
    @GetMapping("/get/ready/{value}")
    public ResponseEntity<List<PostGetResponse>> getReadyPosts(@PathVariable DateStatus value, @AuthenticationPrincipal MyPrincipal principal) {
        return ResponseEntity.ok(postService.getSpecificPostsWithUser(value, RegisterStatus.REGISTER_READY, principal.getUserId()).stream().map(PostGetResponse::toResponse).toList());
    }


    // user 반영
    @GetMapping("/get/success/{value}")
    public ResponseEntity<List<PostGetResponse>> getSuccessPosts(@PathVariable DateStatus value, @AuthenticationPrincipal MyPrincipal principal) {
        return ResponseEntity.ok(postService.getSpecificPostsWithUser(value, RegisterStatus.REGISTER_SUCCESS, principal.getUserId()).stream().map(PostGetResponse::toResponse).toList());
    }


    // user 반영
    @GetMapping("/get/failed/{value}")
    public ResponseEntity<List<PostGetResponse>> getFailedPosts(@PathVariable DateStatus value, @AuthenticationPrincipal MyPrincipal principal) {
        return ResponseEntity.ok(postService.getSpecificPostsWithUser(value, RegisterStatus.REGISTER_FAILED, principal.getUserId()).stream().map(PostGetResponse::toResponse).toList());
    }









    @GetMapping("/check/all")
    public ResponseEntity<List<PostCheckResponse>> getAllPostChecks() {
        return ResponseEntity.ok(postService.getAllPostChecks().stream().map(PostCheckResponse::from).toList());
    }

    @GetMapping("/check/ready")
    public ResponseEntity<List<PostCheckResponse>> getReadyPostChecks() {
        return ResponseEntity.ok(postService.getSpecificPostChecks(RegisterStatus.REGISTER_READY).stream().map(PostCheckResponse::from).toList());
    }

    @GetMapping("/check/success")
    public ResponseEntity<List<PostCheckResponse>> getSuccessPostChecks() {
        return ResponseEntity.ok(postService.getSpecificPostChecks(RegisterStatus.REGISTER_SUCCESS).stream().map(PostCheckResponse::from).toList());
    }

    @GetMapping("/check/failed")
    public ResponseEntity<List<PostCheckResponse>> getFailedPostChecks() {
        return ResponseEntity.ok(postService.getSpecificPostChecks(RegisterStatus.REGISTER_FAILED).stream().map(PostCheckResponse::from).toList());
    }













    // user 반영
    @GetMapping("/get/auction/all")
    public ResponseEntity<List<PostAuctionResponse>> getAllAuctionPosts(@AuthenticationPrincipal MyPrincipal principal) {

        return ResponseEntity.ok(postService.getAllAuctionPosts(principal.getUserId()).stream().map(PostAuctionResponse::toAuctionResponse).toList());
    }

    @GetMapping("/get/auction/all/admin")
    public ResponseEntity<List<PostAuctionResponse>> getAllAuctionPostsAdmin() {

        return ResponseEntity.ok(postService.getAllAuctionPosts().stream().map(PostAuctionResponse::toAuctionResponse).toList());
    }
















    // user 반영
    @GetMapping("/get/auction/ready")
    public ResponseEntity<List<PostAuctionResponse>> getReadyAuctionPosts(@AuthenticationPrincipal MyPrincipal principal) {

        return ResponseEntity.ok(postService.getSpecificAuctionPosts(AuctionStatus.AUCTION_READY, principal.getUserId()).stream().map(PostAuctionResponse::toAuctionResponse).toList());
    }
    @GetMapping("/get/auction/ready/admin")
    public ResponseEntity<List<PostAuctionResponse>> getReadyAuctionPostsAdmin() {

        return ResponseEntity.ok(postService.getSpecificAuctionPosts(AuctionStatus.AUCTION_READY).stream().map(PostAuctionResponse::toAuctionResponse).toList());
    }

    // user 반영
    @GetMapping("/get/auction/current")
    public ResponseEntity<List<PostAuctionResponse>> getCurrentAuctionPosts(@AuthenticationPrincipal MyPrincipal principal) {

        return ResponseEntity.ok(postService.getSpecificAuctionPosts(AuctionStatus.AUCTION_CURRENT, principal.getUserId()).stream().map(PostAuctionResponse::toAuctionResponse).toList());
    }
    @GetMapping("/get/auction/current/admin")
    public ResponseEntity<List<PostAuctionResponse>> getCurrentAuctionPostsAdmin() {

        return ResponseEntity.ok(postService.getSpecificAuctionPosts(AuctionStatus.AUCTION_CURRENT).stream().map(PostAuctionResponse::toAuctionResponse).toList());
    }


    // user 반영
    @GetMapping("/get/auction/finish")
    public ResponseEntity<List<PostAuctionResponse>> getFinishAuctionPosts(@AuthenticationPrincipal MyPrincipal principal) {

        return ResponseEntity.ok(postService.getSpecificAuctionPosts(AuctionStatus.AUCTION_FINISH, principal.getUserId()).stream().map(PostAuctionResponse::toAuctionResponse).toList());
    }
    @GetMapping("/get/auction/finish/admin")
    public ResponseEntity<List<PostAuctionResponse>> getFinishAuctionPostsAdmin() {

        return ResponseEntity.ok(postService.getSpecificAuctionPosts(AuctionStatus.AUCTION_FINISH).stream().map(PostAuctionResponse::toAuctionResponse).toList());
    }





















    // 등록 상태 업데이트
    // 허락 / 거절
    // 추후 코드 수정 필요
    @PostMapping("/update/register/status/true/{id}")
    public ResponseEntity<Boolean> updateRegisterStatusTrue(@PathVariable Long id) {
        return ResponseEntity.ok(postService.updateRegisterStatus(true, id, null));
    }

    @PostMapping("/update/register/status/false/{id}")
    public ResponseEntity<Boolean> updateRegisterStatusFalse(@PathVariable Long id, @RequestBody String failedReason) {
        return ResponseEntity.ok(postService.updateRegisterStatus(false, id, failedReason));
    }








    @GetMapping("/get/sell/list/{value}")
    public ResponseEntity<List<PostSellResponse>> getSellList(@AuthenticationPrincipal MyPrincipal principal, @PathVariable DateStatus value) {
        String userId = principal.getUserId();

        return ResponseEntity.ok(postService.getSellList(userId, value));
    }

    @GetMapping("/get/buy/list/{value}")
    public ResponseEntity<List<PostSellResponse>> getBuyList(@AuthenticationPrincipal MyPrincipal principal, @PathVariable DateStatus value) {
        String userId = principal.getUserId();

        return ResponseEntity.ok(postService.getBuyList(userId, value));
    }

    @GetMapping("/get/recent/{name}/{status}")
    public ResponseEntity<List<ProductDto>> getRecentList(@PathVariable String name, @PathVariable Long status) {
        return ResponseEntity.ok(postService.getRecentList(name, status));
    }

    @GetMapping("/get/receive/user")
    public ResponseEntity<List<PostReceiveResponse>> getReceiveList(@AuthenticationPrincipal MyPrincipal principal) {
        return ResponseEntity.ok(postService.getReceiveList(principal.getUserId()));
    }

    @GetMapping("/get/receive/admin")
    public ResponseEntity<List<PostReceiveResponse>> getReceiveAdminList() {
        return ResponseEntity.ok(postService.getReceiveAdmin());
    }

    @GetMapping("/get/receive/count/user")
    public ResponseEntity<PostAuctionCountResponse> getReceiveCountUser(@AuthenticationPrincipal MyPrincipal principal) {
        String userId = principal.getUserId();
        return ResponseEntity.ok(postService.getPostReceiveUserList(userId));
    }

    @GetMapping("/get/receive/count/admin")
    public ResponseEntity<PostAuctionCountResponse> getReceiveCountAdmin() {
        return ResponseEntity.ok(postService.getPostReceiveAdminList());
    }


    @PostMapping("/update/receive/status/{id}")
    public ResponseEntity<Boolean> updateReceiveStatus(@PathVariable Long id) {
        return ResponseEntity.ok(postService.updateReceiveStatus(id));
    }





    @GetMapping("/get/detail/{id}")
    public ResponseEntity<PostDetailCheckResponse> getDetailResponse(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getDetailResponse(id));
    }



}
