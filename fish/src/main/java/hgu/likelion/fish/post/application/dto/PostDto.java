package hgu.likelion.fish.post.application.dto;

import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.post.presentation.request.PostInfoRequest;
import hgu.likelion.fish.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Long id;
    private String name;
    private int fishCount;
    private String fishWeight;
    private String fishStatus;
    private String salesMethod;
    private int reservePrice;
    private String registrationStatus;
    private String auctionStatus;
    private String commentBySeller;
    private String aiEvaluation;

    private Long sellerId;
    private Long buyerId;

    private String sellerName;
    private String sellerCompany;
    private String origin;
    private List<String> urls;

    public static PostDto from(Post post, List<String> urls) {
        return PostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .fishCount(post.getFishCount())
                .fishWeight(post.getFishWeight())
                .fishStatus(post.getFishStatus())
                .salesMethod(post.getSalesMethod())
                .reservePrice(post.getReservePrice())
                .buyerId(post.getBuyerId())
                .urls(urls)
                .build();
    }

    public static PostDto from(Post post, User user) {
        return PostDto.builder()
                .name(post.getName())
                .origin(user.getOrigin())
                .fishWeight(post.getFishWeight())
                .aiEvaluation(post.getAiEvaluation())
                .reservePrice(post.getReservePrice())
                .sellerName(user.getName())
                .sellerCompany(user.getCompanyName())
                .registrationStatus(post.getRegistrationStatus())
                .build();
    }

    public static PostDto fromInfoAdd(PostInfoRequest postInfoRequest) {
        return PostDto.builder()
                .name(postInfoRequest.getName())
                .fishCount(postInfoRequest.getFishCount())
                .fishWeight(postInfoRequest.getFishWeight())
                .fishStatus(postInfoRequest.getFishStatus())
                .salesMethod(postInfoRequest.getSalesMethod())
                .reservePrice(postInfoRequest.getReservePrice())
                .build();
    }

}
