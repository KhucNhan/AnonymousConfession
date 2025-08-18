package com.example.anonymousconfession.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreatePostRequest {
    private Long id;               // chỉ cần khi update
    private Long categoryId;
    private List<Long> tagIds;
    private String content;
    private String title;
}
