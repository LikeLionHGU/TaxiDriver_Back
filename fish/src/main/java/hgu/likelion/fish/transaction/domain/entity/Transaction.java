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
    private int winningPrice;

    private LocalDateTime settlementDate;
    private int settlementPrice;
    private String settlementStatus;

    private double profitRate;
    private String transactionCode;

    private LocalDateTime receiptDate;
    private int receiptStatus;
    private String receiptLocation;
    private String message;

}
