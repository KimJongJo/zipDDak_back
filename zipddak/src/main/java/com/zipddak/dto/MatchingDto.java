package com.zipddak.dto;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingDto {
    private Integer matchingIdx;
    private String matchingCode;
    private Integer requestIdx;
    private Integer estimateIdx;
    private Integer paymentIdx;
    private Date workStartDate;
    private Date workEndDate;
    private String status;
    private Date createdAt;
}
