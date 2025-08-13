package com.example.anonymousconfession.controller;

import com.example.anonymousconfession.dto.CommentDTO;
import com.example.anonymousconfession.dto.CreateCommentRequest;
import com.example.anonymousconfession.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request, HttpServletRequest httpReq) {
        String ipAddress = getClientIp(httpReq);
        CommentDTO dto = commentService.createComment(request, ipAddress);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        return (xf != null && !xf.isBlank()) ? xf.split(",")[0].trim() : request.getRemoteAddr();
    }
}
