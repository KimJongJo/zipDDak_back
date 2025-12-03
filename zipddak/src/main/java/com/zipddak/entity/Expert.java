package com.zipddak.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Expert {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expertIdx;

    @Column(nullable = false)
    private String userUsername;

    @Column(nullable = false)
    private String activityName;

    @Column
    private Integer profileImageIdx;

    @Column
    private String introduction;

    @Column
    private Integer mainServiceIdx;

    @Column
    private String zonecode;

    @Column
    private String addr1;

    @Column
    private String addr2;

    @Column
    private Integer employeeCount;

    @Column
    private Date contactStartTime;

    @Column
    private Date contactEndTime;

    @Column
    private String externalLink1;

    @Column
    private String externalLink2;

    @Column
    private String externalLink3;

    @Column
    private String providedServiceIdx;  // 콤마 구분 문자열

    @Column(columnDefinition = "TEXT")
    private String providedServiceDesc;

    @Column
    private Integer certImage1Id;

    @Column
    private Integer certImage2Id;

    @Column
    private Integer certImage3Id;

    @Column
    private Integer businessLicensePdfId;

    @Column(columnDefinition = "TEXT")
    private String questionAnswer1;

    @Column(columnDefinition = "TEXT")
    private String questionAnswer2;

    @Column(columnDefinition = "TEXT")
    private String questionAnswer3;

    @Column
    private String settleBank;

    @Column
    private String settleAccount;

    @Column
    private String settleHost;

    @CreationTimestamp
    private Date createdAt;
}
