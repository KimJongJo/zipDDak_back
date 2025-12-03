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
public class Cancel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cancelIdx;

    @Column(nullable = false)
    private Integer paymentIdx;

    @Column(nullable = false)
    private Integer cancelAmount;

    @Column
    private String cancelReason;

    @Column
    private Integer refundableAmount;

    @Column
    private Integer cardDiscountAmount;

    @Column
    private Integer easyPayDiscountAmount;

    @Column
    private Date canceledAt;

    @Column
    private String transactionKey;

    @Column
    private String cancelStatus;
}
