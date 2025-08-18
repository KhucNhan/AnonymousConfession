package com.example.anonymousconfession.controller;

import com.example.anonymousconfession.dto.CreatePostRequest;
import com.example.anonymousconfession.dto.PostDTO;
import com.example.anonymousconfession.dto.UpdatePostRequest;
import com.example.anonymousconfession.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request, HttpServletRequest servletRequest) {
        String ipAddress = servletRequest.getRemoteAddr();
        PostDTO postDTO = postService.createPost(request, ipAddress);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        PostDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody UpdatePostRequest request,
            HttpServletRequest servletRequest) {

        String ipAddress = servletRequest.getRemoteAddr();
        PostDTO updatedPost = postService.updatePost(id, request, ipAddress, request.getEditToken());
        return ResponseEntity.ok(updatedPost);
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deletePost(@PathVariable Long id, HttpServletRequest servletRequest) {
//        String ipAddress = servletRequest.getRemoteAddr();
//        postService.deletePost(id, ipAddress);
//        return ResponseEntity.ok("Post deleted successfully");
//    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id) {
        postService.likeOrDislike(id, true);
        return ResponseEntity.ok("Post liked successfully");
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> dislikePost(@PathVariable Long id) {
        postService.likeOrDislike(id, false);
        return ResponseEntity.ok("Post disliked successfully");
    }

    @GetMapping("/search")
    public List<PostDTO> search(@RequestParam String keyword) {
        return postService.searchByKeyword(keyword);
    }

    @GetMapping("/search/category/{categoryId}")
    public List<PostDTO> searchByCategory(@PathVariable Long categoryId) {
        return postService.searchByCategory(categoryId);
    }

    @GetMapping("/search/tags")
    public ResponseEntity<List<PostDTO>> searchByTags(@RequestParam String query) {
        List<String> tagNames = Arrays.stream(query.split("#"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        System.out.println(tagNames);

        if (tagNames.size() == 1 && isNumeric(tagNames.get(0))) {
            long tagId = Long.parseLong(tagNames.get(0));
            return ResponseEntity.ok(postService.searchByTagId(tagId));
        }

        return ResponseEntity.ok(postService.searchByTagNames(tagNames));
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
