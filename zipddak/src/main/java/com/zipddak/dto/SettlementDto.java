package com.zipddak.dto;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementDto {
    private Integer settlementIdx;
    private String targetUsername;
    private String targetType;
    private Integer amount;
    private String state;
    private Date periodStart;
    private Date periodEnd;
    private Date completedAt;
    private String comment;
}
