package com.zipddak.entity;

import java.sql.Date;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sellerIdx;


    @Column(unique = true)
    private Integer logoFileIdx;

    @Column(nullable = false, unique = true)
    private String compBno; 

    @Column(unique = true)
    private Integer onlinesalesFileIdx;

    @Column(nullable = false)
    private String compName;

    @Column
    private String compHp;

    @Column(nullable = false)
    private String managerName;

    @Column(nullable = false, unique = true)
    private String managerTel;

    @Column(nullable = false, unique = true)
    private String managerEmail;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private String handleItemCateIdx; // 콤마로 구분

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column
    private String pickupZonecode;

    @Column
    private String pickupAddr1;

    @Column
    private String pickupAddr2;

    @Column
    private Long basicPostCharge;

    @Column
    private Long freeChargeAmount;
    
    @Column(nullable = false)
    private Boolean approvalYn;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
