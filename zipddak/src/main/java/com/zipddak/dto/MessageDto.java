package com.zipddak.dto;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private Integer messageIdx;
    private String type;
    private String content;
    private String receiverUsername;
    private String receiverName;
    private String senderUsername;
    private String senderName;
    private Integer toolIdx;
    private Integer estimateIdx;
    private Boolean sendButton;
    private Date createdAt;
}
