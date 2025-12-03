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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productIdx;

    @Column(nullable = false)
    private String sellerUsername;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer thumbnailFileIdx;

    @Column
    private Integer image1FileIdx;

    @Column
    private Integer image2FileIdx;

    @Column
    private Integer image3FileIdx;

    @Column
    private Integer image4FileIdx;

    @Column
    private Integer image5FileIdx;

    @Column(nullable = false)
    private Integer detail1FileIdx;

    @Column
    private Integer detail2FileIdx;

    @Column(nullable = false)
    private Integer categoryIdx;

    @Column
    private Integer subCategoryIdx;

    @Column(nullable = false)
    private Long price;

    @Column
    private Long salePrice;

    @Column
    private Integer discount;

    @Column
    private Boolean optionYn;

    @Column
    private Boolean postYn;

    @Enumerated(EnumType.STRING)
    private PostType postType; // bundle, single

    @Column(nullable = false)
    private Long postCharge;

    @Column
    private Boolean pickupYn;

    @Column
    private String zonecode;

    @Column
    private String pickupAddr1;

    @Column
    private String pickupAddr2;

    @Column(nullable = false)
    private Boolean visibleYn;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public enum PostType {
        bundle, single
    }
}
