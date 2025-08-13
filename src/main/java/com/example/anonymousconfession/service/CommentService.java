package com.example.anonymousconfession.service;

import com.example.anonymousconfession.dto.CommentDTO;
import com.example.anonymousconfession.dto.CreateCommentRequest;
import com.example.anonymousconfession.dto.ReplyDTO;
import com.example.anonymousconfession.model.Comment;
import com.example.anonymousconfession.model.Post;
import com.example.anonymousconfession.repository.CommentRepository;
import com.example.anonymousconfession.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentDTO createComment(CreateCommentRequest request, String ipAddress) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = Comment.builder()
                .post(post)
                .content(request.getContent())
                .ipAddress(ipAddress)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
        return mapToDTO(comment);
    }

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        // (Optional) có thể kiểm tra tồn tại Post trước nếu muốn trả 404 khi post không tồn tại
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        // map replies nếu muốn trả kèm (không bắt buộc ở API list)
        if (comment.getReplies() != null) {
            List<ReplyDTO> replies = comment.getReplies().stream().map(r -> {
                ReplyDTO rd = new ReplyDTO();
                rd.setId(r.getId());
                rd.setContent(r.getContent());
                rd.setCreatedAt(r.getCreatedAt());
                return rd;
            }).collect(Collectors.toList());
            dto.setReplies(replies);
        }

        return dto;
    }
}
