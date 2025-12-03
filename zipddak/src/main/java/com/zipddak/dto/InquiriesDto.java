package com.zipddak.dto;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiriesDto {
    private Integer inquiryIdx;
    private String type;
    private String content;
    private String writerUsername;
    private String writerType;
    private String answererUsername;
    private String answererType;
    private String answer;
    private Integer image1Idx;
    private Integer image2Idx;
    private Integer image3Idx;
    private Integer orderIdx;
    private Integer productIdx;
    private Date writeAt;
    private Date answerAt;
}
