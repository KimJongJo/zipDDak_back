package com.zipddak.entity;

import java.sql.Date;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer settlementIdx;

    @Column(nullable = false)
    private String targetUsername;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementState state;

    @Column(nullable = false)
    private Date periodStart;

    @Column(nullable = false)
    private Date periodEnd;

    @CreationTimestamp
    private Date completedAt;

    @Column
    private String comment;

    public enum TargetType {
        EXPERT, SELLER, USER
    }

    public enum SettlementState {
        PENDING, COMPLETED
    }
}
