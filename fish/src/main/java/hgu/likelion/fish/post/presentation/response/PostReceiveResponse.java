package hgu.likelion.fish.post.presentation.response;

import hgu.likelion.fish.auction.domain.entity.Auction;
import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.commons.image.entity.Image;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReceiveResponse {
    private Long id;
    private String name;
    private Integer totalPrice;

    private String origin;

    private Integer fishCount;
    private String fishWeight;
    private String fishStatus;
    private String salesMethod;



    private Boolean isReceived;
    private LocalDateTime receivedTime;


    private String buyerName;
    private String sellerName;
    private String receiveLocation;

    public static PostReceiveResponse toResponse(Post post) {
        return PostReceiveResponse.builder()
                .id(post.getId())
                .name(post.getName())
                .totalPrice(post.getTotalPrice())
                .origin(post.getOrigin())
                .fishCount(post.getFishCount())
                .fishWeight(post.getFishWeight())
                .fishStatus(post.getFishStatus())
                .salesMethod(post.getSalesMethod())
                .isReceived(post.getIsReceived())
                .receivedTime(post.getReceivedTime())
                .buyerName(post.getBuyer().getName())
                .sellerName(post.getSeller().getName())
                .receiveLocation(post.getSeller().getLocation())
                .build();
    }
}
