package com.zipddak.entity;

import java.sql.Date;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentIdx;

    @Column(nullable = false)
    private String paymentKey;

    @Column
    private String type;

    @Column(nullable = false)
    private String orderId;

    @Column
    private String orderName;

    @Column(nullable = false)
    private String mId;

    @Column
    private String method;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column
    private Integer balanceAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Date requestedAt;

    @Column
    private Date approvedAt;

    @Column
    private String lastTransactionKey;

    @Column
    private Boolean isPartialCancelable;

    @Column
    private String receiptUrl;

    @Column
    private Integer cardAmount;

    @Column
    private String cardIssuerCode;

    @Column
    private String cardAcquirerCode;

    @Column
    private String cardNumber;

    @Column
    private Integer cardInstallmentPlanMonths;

    @Column
    private String easypayProvider;

    @Column
    private Integer easypayAmount;

    public enum PaymentStatus {
        READY,
        IN_PROGRESS,
        WAITING_FOR_DEPOSIT,
        DONE,
        CANCELED,
        PARTIAL_CANCELED,
        ABORTED,
        EXPIRED
    }
}
