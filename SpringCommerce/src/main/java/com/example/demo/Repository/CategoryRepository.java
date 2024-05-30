package com.example.demo.Repository;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // CategoryRepository is responsible for connecting and retrieving product data and also acts as an interface between the DAO and Service classes.

    public Page<Category> findByParentId(UUID parenId, Pageable pageable);
}
