package hgu.likelion.fish.auction.application.dto;

import hgu.likelion.fish.auction.domain.entity.Auction;
import hgu.likelion.fish.auction.presentation.request.AuctionInfoRequest;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.user.application.dto.UserDto;
import hgu.likelion.fish.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionDto {

    private Long id;
    private Integer price;
    private User user;
    private Post post;

    public static AuctionDto fromInfoAdd(Post post, Integer price, User user) {
        return AuctionDto.builder()
                .price(price)
                .user(user)
                .post(post)
                .build();
    }

    public static AuctionDto fromAuction(Auction auction) {
        return AuctionDto.builder()
                .id(auction.getId())
                .price(auction.getPrice())
                .user(auction.getUser())
                .post(auction.getPost())
                .build();
    }
}
