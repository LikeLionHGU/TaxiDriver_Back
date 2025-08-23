package hgu.likelion.fish.auction.presentation.request;


import hgu.likelion.fish.post.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionInfoRequest {

    private Long postId;
    private Integer price;
}
