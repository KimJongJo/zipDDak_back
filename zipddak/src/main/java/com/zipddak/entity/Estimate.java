package com.zipddak.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer estimateIdx;

    @Column(nullable = false)
    private Integer expertIdx;

    @Column(nullable = false)
    private Integer requestIdx;

    @Column(nullable = false)
    private Integer largeServiceIdx;

    @Enumerated(EnumType.STRING)
    private WorkDurationType workDurationType;  // HOUR, DAY, WEEK, MONTH

    @Column
    private Integer workDurationValue;

    @Column(nullable = false)
    private String workScope; // 콤마 구분 문자열

    @Column(columnDefinition = "TEXT")
    private String workDetail;

    @Column
    private Integer disposalCost;

    @Column
    private Integer demolitionCost;

    @Column
    private Integer consultingLaborCost;

    @Column
    private Integer stylingDesignCost;

    @Column
    private Integer threeDImageCost;

    @Column
    private Integer reportProductionCost;

    @Column
    private Integer etcFee;

    @CreationTimestamp
    private Date createdAt;

    public enum WorkDurationType {
        HOUR, DAY, WEEK, MONTH
    }
}
