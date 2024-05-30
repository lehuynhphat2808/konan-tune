package com.example.demo.FolderOfBao.Decorator;

import java.util.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.demo.Model.Product;
import com.example.demo.Service.Implement.ProductService;
import org.springframework.stereotype.Service;


@Service
@CacheConfig(cacheNames = "productsByCategory")
public class ProductServiceDecorator extends ProductService {
    private ProductService productService;

    public ProductServiceDecorator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @Cacheable(key = "{#categoryId, #pageIndex, #pageSize}")
    public Page<Product> getByCategoryId(UUID categoryId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findByCategoryId(categoryId, pageable);
    }

    @Override
    @Cacheable(key = "{#keyword, #pageIndex, #pageSize}")
    public Page<Product> search(String keyword, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.search(keyword, pageable);
    }

    @Override
    @Cacheable(key = "{#pageIndex, #pageSize}")
    public Page<Product> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findAll(pageable);
    }
}
