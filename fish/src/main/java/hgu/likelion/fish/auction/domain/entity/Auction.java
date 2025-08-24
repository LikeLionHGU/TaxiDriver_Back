package hgu.likelion.fish.auction.domain.entity;

import hgu.likelion.fish.auction.application.dto.AuctionDto;
import hgu.likelion.fish.commons.entity.BaseEntity;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Auction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Integer price;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;


    public static Auction fromDto(AuctionDto auctionDto) {
        return Auction.builder()
                .id(auctionDto.getId())
                .user(auctionDto.getUser())
                .price(auctionDto.getPrice())
                .post(auctionDto.getPost())
                .build();

    }
}
