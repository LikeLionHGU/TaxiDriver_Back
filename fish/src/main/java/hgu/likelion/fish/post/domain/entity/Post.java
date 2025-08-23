package hgu.likelion.fish.post.domain.entity;

import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.BaseEntity;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.commons.image.entity.Image;
import hgu.likelion.fish.post.application.dto.PostDto;
import hgu.likelion.fish.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer fishCount;
    private String fishWeight;
    private String fishStatus;
    private String salesMethod;
    private Integer reservePrice;
    private String origin;

    private RegisterStatus registrationStatus;
    private AuctionStatus auctionStatus;

    private String commentBySeller;
    private String aiEvaluation;

    private LocalDateTime triggerAt;
    private LocalDateTime startedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private User seller;

    @OneToMany(
            mappedBy = "post",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    public static Post fromDto(PostDto postDto, List<Image> images, User user) {
        return Post.builder()
                .name(postDto.getName())
                .fishCount(postDto.getFishCount())
                .fishWeight(postDto.getFishWeight())
                .fishStatus(postDto.getFishStatus())
                .salesMethod(postDto.getSalesMethod())
                .reservePrice(postDto.getReservePrice())
                .images(images)
                .seller(user)
                .build();
    }

}
