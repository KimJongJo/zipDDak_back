package com.zipddak.entity;

import java.sql.Date;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class User {

    @Id
    @Column(nullable = false)
    private String username; 

    @Column(nullable = false)
    private String nickname;

    @Column
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column
    private String zonecode;

    @Column
    private String addr1;

    @Column
    private String addr2;

    @Column
    private String settleBank;

    @Column
    private String settleAccount;

    @Column
    private String settleHost;

    @Column
    private String provider;

    @Column
    private String providerId;

    @Column
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private UserRole role; // USER, ADMIN

    @Column
    private Boolean expert; // 전문가 전환 여부

    @CreationTimestamp
    private Date createdate;

    @Column
    private Integer profileImg;

    public enum UserRole {
        USER, ADMIN
    }
}
