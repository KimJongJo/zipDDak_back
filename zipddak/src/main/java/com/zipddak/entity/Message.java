package com.zipddak.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageIdx;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type; // TOOL, EXPERT

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String receiverUsername;

    @Column
    private String receiverName;

    @Column(nullable = false)
    private String senderUsername;

    @Column
    private String senderName;

    @Column
    private Integer toolIdx;

    @Column
    private Integer estimateIdx;

    @Column
    private Boolean sendButton;

    @CreationTimestamp
    private Date createdAt;

    public enum MessageType {
        TOOL, EXPERT
    }
}
