package com.zipddak.dto;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioDto {
    private Integer portfolioIdx;
    private Integer expertIdx;
    private String title;
    private Integer serviceIdx;
    private String region;
    private Integer price;
    private String workTimeType;
    private Integer workTimeValue;
    private Integer image1Idx;
    private Integer image2Idx;
    private Integer image3Idx;
    private String description;
    private Date createdAt;
}
