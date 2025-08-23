package hgu.likelion.fish.auction.presentation.response;

import hgu.likelion.fish.auction.application.dto.AuctionDto;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.user.application.dto.UserDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionGetResponse {

    private String name;
    private Integer price;

    public static AuctionGetResponse fromAuctionDto(AuctionDto auctionDto) {
        return AuctionGetResponse.builder()
                .name(auctionDto.getUser().getName())
                .price(auctionDto.getPrice())
                .build();
    }
}
