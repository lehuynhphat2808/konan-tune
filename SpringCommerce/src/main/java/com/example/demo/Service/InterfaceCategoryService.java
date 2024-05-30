package com.example.demo.Service;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface InterfaceCategoryService extends InterfaceBaseService<Category, CategoryRepository>{
    public Page<Category> getByParentId(UUID parentId, int pageIndex, int pageSize);
}
