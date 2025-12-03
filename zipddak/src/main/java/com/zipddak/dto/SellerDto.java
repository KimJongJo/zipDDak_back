package com.zipddak.dto;

import java.sql.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDto {
    private String username;
    private String password;
    private String name;
    private String phone;
    private Integer logoFileIdx;
    private String compBno;
    private Integer profileFileIdx;
    private Integer onlinesalesFileIdx;
    private String compName;
    private String compHp;
    private String managerName;
    private String managerTel;
    private String managerEmail;
    private String brandName;
    private String handleItemCateIdx;
    private String introduction;
    private String settleBank;
    private String settleAccount;
    private String settleHost;
    private String compZonecode;
    private String compAddr1;
    private String compAddr2;
    private String pickupZonecode;
    private String pickupAddr1;
    private String pickupAddr2;
    private Long basicPostCharge;
    private Long freeChargeAmount;
    private Boolean approvalYn;
    private Date createdAt;
    private Date updatedAt;
}
