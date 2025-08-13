package com.example.anonymousconfession.dto;

import lombok.Data;

@Data
public class LikeDislikeRequest {
    private Long postId;
    private boolean like; // true = like, false = dislike
}
