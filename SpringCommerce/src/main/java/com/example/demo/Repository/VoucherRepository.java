package com.example.demo.Repository;

import com.example.demo.Model.Product;
import com.example.demo.Model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    // VoucherRepository is responsible for connecting and retrieving product data and also acts as an interface between the DAO and Service classes.

    public boolean existsByCodeAndUsers_Id(String code, UUID userId);
    public boolean existsByCode(String code);
    public Optional<Voucher> findByCode(String code);


}
