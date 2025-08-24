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

    private LocalDateTime winningDate; // 낙찰일
    private Integer winningPrice; // 낙찰가

    private LocalDateTime settlementDate; // 정산일
    private Integer settlementPrice; // 정산가
    private String settlementStatus; // 정산현황

    private Double profitRate; //
    private String transactionCode;

    private LocalDateTime receiptDate;
    private Integer receiptStatus;
    private String receiptLocation;
    private String message;

}
