package com.example.anonymousconfession.service;

import com.example.anonymousconfession.dto.CreatePostRequest;
import com.example.anonymousconfession.dto.PostDTO;
import com.example.anonymousconfession.dto.UpdatePostRequest;
import com.example.anonymousconfession.model.Category;
import com.example.anonymousconfession.model.Post;
import com.example.anonymousconfession.model.Tag;
import com.example.anonymousconfession.repository.CategoryRepository;
import com.example.anonymousconfession.repository.PostRepository;
import com.example.anonymousconfession.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PostDTO getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public PostDTO createPost(CreatePostRequest request, String ipAddress) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Tag> tags = tagRepository.findAllById(request.getTagIds());

        String token = UUID.randomUUID().toString();

        Post post = Post.builder()
                .category(category)
                .tags(tags)
                .content(request.getContent())
                .ipAddress(ipAddress)
                .editToken(token)
                .likeCount(0)
                .dislikeCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postRepository.save(post);

        PostDTO dto = mapToDTO(post);
        dto.setEditToken(token); // chỉ trả về khi tạo
        return dto;
    }

    public PostDTO updatePost(Long id, UpdatePostRequest request, String ipAddress, String token) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Kiểm tra IP
        if (!post.getIpAddress().equals(ipAddress)) {
            throw new RuntimeException("Invalid IP address");
        }

        // Kiểm tra token
        if (!post.getEditToken().equals(token)) {
            throw new RuntimeException("Invalid edit token");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Tag> tags = tagRepository.findAllById(request.getTagIds());

        post.setCategory(category);
        post.setTags(tags);
        post.setContent(request.getContent());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);

        return mapToDTO(post);
    }


    public void likeOrDislike(Long postId, boolean like) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (like) {
            post.setLikeCount(post.getLikeCount() + 1);
        } else {
            post.setDislikeCount(post.getDislikeCount() + 1);
        }
        postRepository.save(post);
    }

    private PostDTO mapToDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .categoryId(post.getCategory().getId())
                .categoryName(post.getCategory().getName())
                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .dislikeCount(post.getDislikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
