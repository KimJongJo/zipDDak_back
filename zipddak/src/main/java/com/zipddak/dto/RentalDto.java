package com.zipddak.dto;

import java.sql.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalDto {
    private Integer rentalIdx;
    private String rentalCode;
    private Date startDate;
    private Date endDate;
    private String request;
    private Boolean directRental;
    private Integer postCharge;
    private Boolean postRental;
    private String zonecode;
    private String addr1;
    private String addr2;
    private String postRequest;
    private Boolean paymentType;
    private String satus;
    private String borrower;
    private String owner;
    private Integer paymentIdx;
    private Date createdAt;
    
    private Integer toolIdx;
    private String name;
}
