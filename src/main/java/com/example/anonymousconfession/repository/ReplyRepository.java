package com.example.anonymousconfession.repository;

import com.example.anonymousconfession.model.Comment;
import com.example.anonymousconfession.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByParentCommentIdOrderByCreatedAtDesc(Long parentCommentId);
}
