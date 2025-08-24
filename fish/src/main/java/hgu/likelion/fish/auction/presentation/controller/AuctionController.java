package hgu.likelion.fish.auction.presentation.controller;

import hgu.likelion.fish.auction.application.dto.AuctionDto;
import hgu.likelion.fish.auction.application.service.AuctionService;
import hgu.likelion.fish.auction.domain.entity.Auction;
import hgu.likelion.fish.auction.presentation.request.AuctionInfoRequest;
import hgu.likelion.fish.auction.presentation.response.AuctionAddResponse;
import hgu.likelion.fish.auction.presentation.response.AuctionGetResponse;
import hgu.likelion.fish.auction.presentation.response.AuctionStatusResponse;
import hgu.likelion.fish.commons.jwt.MyPrincipal;
import hgu.likelion.fish.post.application.service.PostService;
import hgu.likelion.fish.user.application.service.UserService;
import hgu.likelion.fish.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final PostService postService;
    private final UserService userService;
    private final AuctionService auctionService;

    @PostMapping("/add")
    public ResponseEntity<AuctionAddResponse> addAuction(
            @AuthenticationPrincipal MyPrincipal principal,
            @RequestBody AuctionInfoRequest info) {
        AuctionAddResponse response = new AuctionAddResponse();

        String userId = principal.getUserId();

        User user = userService.findUserById(userId);

        AuctionDto auctionDto = auctionService.saveAuction(info, user);
        if (auctionDto == null) {
            response.setIsSuccess(0);
        } else {
            response.setIsSuccess(1);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/specific/users/{postId}")
    public ResponseEntity<List<AuctionGetResponse>> getUserAuction(@PathVariable Long postId) {
        return ResponseEntity.ok(auctionService.getAllUserAuction(postId));
    }

    


}
