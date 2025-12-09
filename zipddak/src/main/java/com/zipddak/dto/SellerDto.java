package com.zipddak.dto;

import java.sql.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDto {
	private Integer sellerIdx;
	private String sellerUsername; 
    private String password;
    private String name;
    private String phone;
    private Integer logoFileIdx;
    private String compBno;
    private Integer compFileIdx;	//사업자등록증 추가
//    private Integer profileFileIdx;	//logofile과 중복되는 듯
    private Integer onlinesalesFileIdx;
    private String compName;
    private String compHp;
    private String ceoName;
    private String managerName;
    private String managerTel;
    private String managerEmail;
    private String brandName;
    private String handleItemCateIdx;
    private String introduction;
    private String settleBank;
    private String settleAccount;
    private String settleHost;
    private String zonecode;	//userTable과 동일하게 컬럼명 수정
    private String addr1;	//userTable과 동일하게 컬럼명 수정
    private String addr2;	//userTable과 동일하게 컬럼명 수정
    private String pickupZonecode;
    private String pickupAddr1;
    private String pickupAddr2;
    private Long basicPostCharge;
    private Long freeChargeAmount;
    private String role;
    private Boolean approvalYn;
    private Date createdate;	//userTable과 동일하게 컬럼명 수정
    private Date updatedAt;
}
