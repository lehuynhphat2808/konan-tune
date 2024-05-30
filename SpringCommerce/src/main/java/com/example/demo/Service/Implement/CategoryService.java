package com.example.demo.Service.Implement;

import com.example.demo.Model.Category;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Service.InterfaceCategoryService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService extends BaseService<Category, CategoryRepository> implements InterfaceCategoryService {
    public Page<Category> getByParentId(UUID parentId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findByParentId(parentId, pageable);
    }

    @Override
    public void deleteById(UUID id) throws NoSuchElementException, NotImplementedException {
        Optional<Category> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            if(category.getProducts().isEmpty()) {
                repository.deleteById(id);
            } else {
                throw new NotImplementedException("Cannot remove category have product");
            }
        } else {
            throw new NoSuchElementException("Can't found productId: " + id + " to delete");
        }
    }
}
