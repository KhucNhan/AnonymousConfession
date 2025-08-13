    package com.example.anonymousconfession.model;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDateTime;
    import java.util.List;

    @Entity
    @Table(name = "comments")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id")
        private Post post;

        @Column(columnDefinition = "TEXT")
        private String content;

        private String ipAddress; // để nhận diện người bình luận

        private LocalDateTime createdAt;

        @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Reply> replies;
    }
