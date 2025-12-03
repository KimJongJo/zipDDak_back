package com.zipddak.dto;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Integer productIdx;
    private String sellerUsername;
    private String name;
    private Integer thumbnailFileIdx;
    private Integer image1FileIdx;
    private Integer image2FileIdx;
    private Integer image3FileIdx;
    private Integer image4FileIdx;
    private Integer image5FileIdx;
    private Integer detail1FileIdx;
    private Integer detail2FileIdx;
    private Integer categoryIdx;
    private Integer subCategoryIdx;
    private Long price;
    private Long salePrice;
    private Integer discount;
    private Boolean optionYn;
    private Boolean postYn;
    private String postType;
    private Long postCharge;
    private Boolean pickupYn;
    private String zonecode;
    private String pickupAddr1;
    private String pickupAddr2;
    private Boolean visibleYn;
    private Date createdAt;
    private Date updatedAt;
}
