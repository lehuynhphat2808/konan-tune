package com.example.demo.Service;

import com.example.demo.Model.Comment;
import com.example.demo.Utilities.CommentUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.NoSuchElementException;
import java.util.UUID;

public interface InterfaceCommentService {
    public boolean insertComment(Comment newComment);
    public Page<Comment> getByProductId(UUID productId, int pageIndex, int pageSize);
    public Comment getByUserIdAndProductId(UUID userId, UUID productId);
    public boolean updateInfo(UUID commentId, CommentUpdateRequest commentUpdateRequest);

}
