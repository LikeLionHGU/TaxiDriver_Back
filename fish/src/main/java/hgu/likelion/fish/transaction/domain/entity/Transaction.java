package hgu.likelion.fish.transaction.domain.entity;

import hgu.likelion.fish.commons.entity.BaseEntity;
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
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime winningDate;
    private Integer winningPrice;

    private LocalDateTime settlementDate;
    private Integer settlementPrice;
    private String settlementStatus;

    private Double profitRate;
    private String transactionCode;

    private LocalDateTime receiptDate;
    private Integer receiptStatus;
    private String receiptLocation;
    private String message;

}
