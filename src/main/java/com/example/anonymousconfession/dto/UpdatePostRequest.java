package com.example.anonymousconfession.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePostRequest {
    private Long categoryId;
    private List<Long> tagIds;
    private String content;
    private String editToken; // Người đăng giữ token này
}

