package com.zipddak.user.dto;

import java.sql.Date;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpertInsertDto {
	
    private String userUsername;
    private String activityName;
    private String zonecode;
    private String addr1;
    private String addr2;
    private Integer employeeCount;
    private String providedServiceIdx;
    private String businessLicense;
    private Integer businessLicensePdfId;
    private String settleBank;
    private String settleAccount;
    private String settleHost;
    private Date createdAt;

}
