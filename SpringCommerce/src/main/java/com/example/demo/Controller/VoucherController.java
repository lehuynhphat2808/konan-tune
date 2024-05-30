package com.example.demo.Controller;

import com.example.demo.Model.Voucher;
import com.example.demo.Service.Implement.JavaMailService;
import com.example.demo.Service.Implement.VoucherService;
import com.example.demo.Utilities.PaginatedResponse;
import com.example.demo.Utilities.Response;
import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class VoucherController {

    final
    VoucherService voucherService;
    final
    JavaMailService javaMailService;

    public VoucherController(JavaMailService javaMailService, VoucherService voucherService) {
        this.javaMailService = javaMailService;
        this.voucherService = voucherService;
    }

    @JsonView(Views.Public.class)
    @GetMapping("/vouchers/page")
    public ResponseEntity<?> getPage(
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Voucher> voucherPage = voucherService.getPage(pageIndex, pageSize);
        PaginatedResponse<Voucher> paginatedResponse = new PaginatedResponse<>(
                voucherPage.getContent(), voucherPage.getTotalElements(), voucherPage.getTotalPages()
        );
        return Response.createResponse(HttpStatus.OK, "get page successfully", paginatedResponse);
    }


    @GetMapping("/voucher/applyVoucher")
    public ResponseEntity<?> applyVoucher(
            @RequestParam String code,
            @RequestParam UUID userId
    ) {
        voucherService.attachObserver(javaMailService);
        //voucherService.attachObserver(smsService);
        return Response.createResponse(HttpStatus.OK, "check used", voucherService.applyVoucher(code, userId));
    }

    @PostMapping("/voucher/insert")
    public ResponseEntity<?> insert(
            @RequestBody Voucher voucher
    ) throws NoSuchElementException {
        try {
            return Response.createResponse(
                    HttpStatus.OK,
                    "insert voucher successfully",
                    voucherService.create(voucher)
            );
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @PutMapping("/voucher/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody Voucher newVoucher
    ) throws NoSuchElementException {
        try {
            return Response.createResponse(
                    HttpStatus.OK,
                    "insert voucher successfully",
                    voucherService.updateVoucher(id, newVoucher)
            );
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @JsonView(Views.Public.class)
    @GetMapping("/voucher/{id}")
    public ResponseEntity<?> getById(
            @PathVariable UUID id
    ) throws NoSuchElementException {
        try {
            return Response.createResponse(
                    HttpStatus.OK,
                    "insert voucher successfully",
                    voucherService.getById(id)
            );
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @PostMapping("/voucher/addUser")
    public ResponseEntity<?> addUser(
            @RequestParam UUID userId,
            @RequestParam String code
    ) throws NoSuchElementException {
        try {
            return Response.createResponse(
                    HttpStatus.OK,
                    "add User successfully",
                    voucherService.addUser(userId, code)
            );
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
}
