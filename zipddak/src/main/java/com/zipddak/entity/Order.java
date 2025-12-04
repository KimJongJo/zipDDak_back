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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderIdx;

    @Column(nullable = false, unique = true)
    private String orderCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userUsername")
    private User user;

    @Column(nullable = false)
    private Long subtotalAmount;

    @Column(nullable = false)
    private Long shippingAmount;

    @Column(nullable = false)
    private Long totalAmount;

    @Column(nullable = false)
    private Integer paymentIdx;

    @Column
    private String postZonecode;

    @Column
    private String postAddr1;

    @Column
    private String postAddr2;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String postRecipient;

    @Column(columnDefinition = "TEXT")
    private String postNote;

    @CreationTimestamp
    private Date createdAt;
}
