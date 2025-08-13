package com.example.anonymousconfession.controller;

import com.example.anonymousconfession.dto.CreateReplyRequest;
import com.example.anonymousconfession.dto.ReplyDTO;
import com.example.anonymousconfession.service.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<?> createReply(@RequestBody CreateReplyRequest request, HttpServletRequest httpReq) {
        String ipAddress = getClientIp(httpReq);
        ReplyDTO dto = replyService.createReply(request, ipAddress);
        return ResponseEntity.ok(dto);
    }

    // >>> Endpoint GET replies theo commentId
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<ReplyDTO>> getRepliesByCommentId(@PathVariable Long commentId) {
        return ResponseEntity.ok(replyService.getRepliesByCommentId(commentId));
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        return (xf != null && !xf.isBlank()) ? xf.split(",")[0].trim() : request.getRemoteAddr();
    }
}
