package com.example.anonymousconfession.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
}
