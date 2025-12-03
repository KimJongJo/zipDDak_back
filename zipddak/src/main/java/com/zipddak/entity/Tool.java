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
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer toolIdx;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer category;

    @Column(nullable = false)
    private Long rentalPrice;

    @Column
    private Boolean freeRental;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String tradeAddr;

    @Column(nullable = false)
    private Boolean directRental;

    @Column(nullable = false)
    private Boolean postRental;

    @Column
    private Date freePost;

    @Column
    private Long postCharge;

    @Column(nullable = false)
    private String zonecode;

    @Column(nullable = false)
    private String addr1;

    @Column(nullable = false)
    private String addr2;

    @Column
    private String postRequest;

    @Enumerated(EnumType.STRING)
    private ToolStatus satus;

    @Column
    private String owner;

    @CreationTimestamp
    private Date createdate;

    @Column
    private Integer thunbnail;

    @Column
    private Integer img1;

    @Column
    private Integer img2;

    @Column
    private Integer img3;

    @Column
    private Integer img4;

    @Column
    private Integer img5;

    public enum ToolStatus {
        ABLE, INABLE, DELETE
    }
}
