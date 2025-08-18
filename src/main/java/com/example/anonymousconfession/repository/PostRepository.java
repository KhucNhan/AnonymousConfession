package com.example.anonymousconfession.repository;

import com.example.anonymousconfession.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Post> searchByTitleOrContent(@Param("keyword") String keyword);

    @Query("SELECT p FROM Post p WHERE p.category.id = :categoryId")
    List<Post> findByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE t.id = :tagId")
    List<Post> findByTag(@Param("tagId") long tagId);

    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE t.name = :tagNames")
    List<Post> findByAnyTagNames(@Param("tagNames") String tagName);

    @Query("SELECT p FROM Post p JOIN p.tags t " +
            "WHERE t.name IN :tagNames " +
            "GROUP BY p.id " +
            "HAVING COUNT(DISTINCT t.name) = :tagCount")
    List<Post> findByAllTagNames(@Param("tagNames") List<String> tagNames,
                                 @Param("tagCount") long tagCount);
}

