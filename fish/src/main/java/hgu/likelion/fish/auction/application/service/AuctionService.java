package hgu.likelion.fish.auction.application.service;

import hgu.likelion.fish.auction.application.dto.AuctionDto;
import hgu.likelion.fish.auction.domain.entity.Auction;
import hgu.likelion.fish.auction.domain.repository.AuctionRepository;
import hgu.likelion.fish.auction.presentation.request.AuctionInfoRequest;
import hgu.likelion.fish.auction.presentation.response.AuctionGetResponse;
import hgu.likelion.fish.auction.presentation.response.AuctionStatusResponse;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.post.domain.repository.PostRepository;
import hgu.likelion.fish.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final PostRepository postRepository;

    @Transactional
    public AuctionDto saveAuction(AuctionInfoRequest info, User user) {
        Post post = postRepository.findById(info.getPostId()).orElse(null);
        AuctionDto auctionDto = AuctionDto.fromInfoAdd(post, info.getPrice(), user);
        Auction auction = Auction.fromDto(auctionDto);

        auctionRepository.save(auction);
        return AuctionDto.fromAuction(auction);
    }

    @Transactional
    public List<AuctionGetResponse> getAllUserAuction(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<Auction> auctionList = auctionRepository.findAllByPost(post);
        List<AuctionDto> dtoList = auctionList.stream().map(AuctionDto::fromAuction).toList();
        return dtoList.stream().map(AuctionGetResponse::fromAuctionDto).toList();
    }

}
