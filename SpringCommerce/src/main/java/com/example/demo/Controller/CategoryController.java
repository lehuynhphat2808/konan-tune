package com.example.demo.Controller;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Service.InterfaceCategoryService;
import com.example.demo.Utilities.Response;
import com.example.demo.Service.Implement.CategoryService;
import com.example.demo.Utilities.PaginatedResponse;
import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CategoryController {
    //Using Interface and DI allows to easily change/extend dependencies without modifying the modules in use.
    @Autowired
    InterfaceCategoryService categoryService;

    @JsonView(Views.Public.class)
    @GetMapping("/categories")
    public ResponseEntity<?> getAll() {
        return Response.createResponse(HttpStatus.OK, "get all category successfully", categoryService.getAll());
    }

    @JsonView(Views.Public.class)
    @GetMapping("/categories/page")
    public ResponseEntity<?> getPage(
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Category> categoryPage = categoryService.getPage(pageIndex, pageSize);
        PaginatedResponse<Category> paginatedResponse = new PaginatedResponse<>(
                categoryPage.getContent(), categoryPage.getTotalElements(), categoryPage.getTotalPages()
        );
        return Response.createResponse(HttpStatus.OK, "get products successfully", paginatedResponse);

    }


    @GetMapping("/category/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            Category category = categoryService.getById(id);
            return Response.createResponse(HttpStatus.OK, "get category by id successfully", category);
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @PostMapping("/category/insert")
    public ResponseEntity<?> insert(@RequestBody Category category) {
        categoryService.insert(category);
        return Response.createResponse(HttpStatus.OK, "insert category successfully", category);
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws NoSuchElementException, NotImplementedException {
        try {
            categoryService.deleteById(id);
            return Response.createResponse(HttpStatus.OK,
                    "deleted category have id: " + id.toString(),
                    null);

        } catch (Exception e) {
            return Response.createResponse(HttpStatus.OK, e.getMessage(), null);
        }
    }

    @JsonView(Views.Public.class)
    @PutMapping("/category/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Category newCategory) {
        try {
            return Response.createResponse(HttpStatus.OK, "update category successfully", categoryService.update(id, newCategory));
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }

    @JsonView(Views.Public.class)
    @GetMapping("/category/getByParentId/{parentId}")
    public ResponseEntity<?> getByParentId(
            @PathVariable UUID parentId,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Category> categoriePage = categoryService.getByParentId(parentId, pageIndex, pageSize);
        PaginatedResponse<Category> paginatedResponse = new PaginatedResponse<>(
                categoriePage.getContent(),
                categoriePage.getTotalElements(),
                categoriePage.getTotalPages()
        );
        return Response.createResponse(
                HttpStatus.OK, "get category by parent id: " + parentId,
                paginatedResponse
        );
    }
}
