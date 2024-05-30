package com.example.demo.Repository;

import com.example.demo.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    // ProductRepository is responsible for connecting and retrieving product data and also acts as an interface between the DAO and Service classes.
    public Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);
    public Page<Product> findByUserId(UUID userId, Pageable pageable);
    @Query("SELECT p from Product p WHERE p.name LIKE CONCAT('%', LOWER(:keyword), '%')")
    public Page<Product> search(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT p from Product p WHERE p.deleted = false ")
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE"
            + "(:categoryId IS NULL OR p.category.id = :categoryId) "
            + "AND (:keyword IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:keyword), '%')) "
            + "AND (:brand IS NULL OR LOWER(p.brand) LIKE CONCAT('%', LOWER(:brand), '%')) "
            + "AND (:color IS NULL OR LOWER(p.color) LIKE CONCAT('%', LOWER(:color), '%'))"
            + "AND (:minPrice IS NULL OR p.price >= :minPrice) "
            + "AND (:maxPrice IS NULL OR p.price <= :maxPrice)"
            + "AND p.deleted = :deleted")
    public Page<Product> filter(
            @Param("categoryId") UUID categoryId,
            @Param("keyword") String keyword,
            @Param("brand") String brand,
            @Param("color") String color,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("deleted") boolean deleted,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p ORDER BY p.selled DESC")
    public Page<Product> findHighestSelled(Pageable pageable);


}
