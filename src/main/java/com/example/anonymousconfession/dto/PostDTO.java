package com.example.anonymousconfession.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    private Long categoryId;
    private String categoryName; // để client dễ hiển thị
    private List<String> tags;   // chỉ trả về tên tag, không trả entity
    private String content;
    private String title;
    private int likeCount;
    private int dislikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String editToken; // chỉ set khi tạo bài
}
