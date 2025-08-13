package com.example.anonymousconfession.dto;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private Long postId;
    private String content;
}
