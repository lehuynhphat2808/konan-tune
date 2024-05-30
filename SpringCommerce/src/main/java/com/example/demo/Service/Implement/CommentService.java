package com.example.demo.Service.Implement;

import com.example.demo.Model.Comment;
import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.InterfaceCommentService;
import com.example.demo.Utilities.CommentUpdateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService extends BaseService<Comment, CommentRepository> implements InterfaceCommentService {
    @Autowired
    CommentRepository newCommentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public boolean insertComment(Comment newComment) throws NoSuchElementException {
        UUID userId = newComment.getUserId();
        if(userId != null) {
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                newComment.setUser(user);
            } else {
                throw new NoSuchElementException("userId is not exists");
            }
        } else {
            throw new NoSuchElementException("userId cannot null");
        }
        UUID productId = newComment.getProductId();
        if(productId != null) {
            Optional<Product> productOptional = productRepository.findById(productId);
            if(productOptional.isPresent()) {
                Product product = productOptional.get();
                newComment.setProduct(product);
            } else {
                throw new NoSuchElementException("productId is not exists");
            }
        } else {
            throw new NoSuchElementException("productId cannot null");
        }
        repository.save(newComment);
        return true;
    }

    @Override
    public Page<Comment> getByProductId(UUID productId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findByProduct_Id(productId, pageable);
    }

    @Override
    public Comment getByUserIdAndProductId(UUID userId, UUID productId) {
        return repository.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean updateInfo(UUID commentId, CommentUpdateRequest commentUpdateRequest) throws NoSuchElementException{
        Optional<Comment> commentOptional = repository.findById(commentId);
        if(commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setCmt(commentUpdateRequest.getNewStringComment());
            comment.setRate(commentUpdateRequest.getRate());
            repository.save(comment);
            return true;
        } else {
            throw new NoSuchElementException("commentId: " + commentId + " is not exits");
        }
    }
}
