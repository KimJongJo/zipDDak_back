package com.zipddak.dto;

import java.sql.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewProductDto {
    private Integer reviewProductIdx;
    private Integer score;
    private String content;
    private Date createdate;
    private String writer;
    private Integer productIdx;
    private Integer orderIdx;
    private Integer img1;
    private Integer img2;
    private Integer img3;
}
