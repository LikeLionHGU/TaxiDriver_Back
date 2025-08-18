package hgu.likelion.fish.user.domain.entity;

import hgu.likelion.fish.commons.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private int userStatus;
    private int bidCount;
    private String phoneNumber;
    private int totalBuyPrice;
    private int totalSellPrice;
    private String companyName;

}
