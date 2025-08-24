package hgu.likelion.fish.auction.presentation.controller;

import hgu.likelion.fish.auction.application.service.AuctionService;
import hgu.likelion.fish.post.application.service.PostService;
import hgu.likelion.fish.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final PostService postService;
    private final UserService userService;
    private final AuctionService auctionService;

}
