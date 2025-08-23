package hgu.likelion.fish.user.domain.entity;

import hgu.likelion.fish.auction.domain.entity.Auction;
import hgu.likelion.fish.commons.entity.BaseEntity;
import hgu.likelion.fish.commons.login.config.Role;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.user.application.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {

    @Id
    private String id;
    private int userStatus;
    private int bidCount;
    private String phoneNumber;
    private int totalBuyPrice;
    private int totalSellPrice;
    private String companyName;
    private String origin;
    private String email;
    private String name;
    private String location;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Post> postSellList;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Post> postBuyList;


    // ★ Enum 컬렉션으로 권한 관리
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();   // @Builder.Default 반드시!

    // 편의 메서드
    public void addRole(Role role) {
        if (roles == null) roles = new HashSet<>();
        roles.add(role);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Auction auction;


}
