package com.example.anonymousconfession.service;

import com.example.anonymousconfession.dto.CreateReplyRequest;
import com.example.anonymousconfession.dto.ReplyDTO;
import com.example.anonymousconfession.model.Comment;
import com.example.anonymousconfession.model.Reply;
import com.example.anonymousconfession.repository.CommentRepository;
import com.example.anonymousconfession.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    public ReplyDTO createReply(CreateReplyRequest request, String ipAddress) {
        Comment parentComment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Reply reply = Reply.builder()
                .parentComment(parentComment)
                .content(request.getContent())
                .ipAddress(ipAddress)
                .createdAt(LocalDateTime.now())
                .build();

        replyRepository.save(reply);

        ReplyDTO dto = new ReplyDTO();
        dto.setId(reply.getId());
        dto.setContent(reply.getContent());
        dto.setCreatedAt(reply.getCreatedAt());
        return dto;
    }

    // >>> HÀM BẠN ĐANG CẦN <<<
    public List<ReplyDTO> getRepliesByCommentId(Long commentId) {
        return replyRepository.findByParentCommentIdOrderByCreatedAtDesc(commentId)
                .stream()
                .map(r -> {
                    ReplyDTO dto = new ReplyDTO();
                    dto.setId(r.getId());
                    dto.setContent(r.getContent());
                    dto.setCreatedAt(r.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
