package com.example.anonymousconfession.dto;

import lombok.Data;

@Data
public class CreateReplyRequest {
    private Long commentId;
    private String content;
}
