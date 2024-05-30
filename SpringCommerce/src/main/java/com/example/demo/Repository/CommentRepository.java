package com.example.demo.Repository;

import com.example.demo.Model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    // CommentRepository is responsible for connecting and retrieving product data and also acts as an interface between the DAO and Service classes.

    Page<Comment> findByProduct_Id(UUID productId, Pageable pageable);
    Comment findByUserIdAndProductId(UUID userId, UUID productId);
}
