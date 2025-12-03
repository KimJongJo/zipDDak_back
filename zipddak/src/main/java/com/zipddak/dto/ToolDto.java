package com.zipddak.dto;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolDto {
    private Integer toolIdx;
    private String name;
    private Integer category;
    private Long rentalPrice;
    private Boolean freeRental;
    private String content;
    private String tradeAddr;
    private Boolean directRental;
    private Boolean postRental;
    private Date freePost;
    private Long postCharge;
    private String zonecode;
    private String addr1;
    private String addr2;
    private String postRequest;
    private String satus;
    private String owner;
    private Date createdate;
    private Integer thunbnail;
    private Integer img1;
    private Integer img2;
    private Integer img3;
    private Integer img4;
    private Integer img5;
}
