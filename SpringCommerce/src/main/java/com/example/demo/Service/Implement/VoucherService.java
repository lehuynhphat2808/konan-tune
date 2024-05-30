package com.example.demo.Service.Implement;

import com.example.demo.Model.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.InterfaceOrderService;
import com.example.demo.nntFolder.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoucherService extends BaseService<Voucher, VoucherRepository>{
    @Autowired
    UserRepository userRepository;
    private final List<Observer> observers = new ArrayList<>();

    public boolean create(Voucher voucher) throws NoSuchElementException {
        repository.save(voucher);
        return true;
    }

    public boolean updateVoucher(UUID voucherId, Voucher newVoucher) throws NoSuchElementException {
        Optional<Voucher> voucherOptional = repository.findById(voucherId);
        if (voucherOptional.isPresent()) {
            newVoucher.setId(voucherId);
            repository.save(newVoucher);
            return true;
        } else {
            throw new NoSuchElementException("voucherId: " + voucherId + "is not exists");
        }

    }


    public Voucher applyVoucher(String code, UUID userId) throws NoSuchElementException {
        Optional<Voucher> voucherOptional = repository.findByCode(code);
        boolean isUsed = repository.existsByCodeAndUsers_Id(code, userId);
        if(voucherOptional.isPresent() && !isUsed) {
            // notify observer
            System.out.println("OBSERVER: " + observers.size());
            for (Observer observer : observers) {
                System.out.println("OBSERVER: " + observer.getClass().getName());
                observer.onVoucherApply(userId, code);
            }
            observers.clear();
            return voucherOptional.get();
        } else {
            return null;
        }
    }

    public boolean addUser(UUID userId, String code) throws NoSuchElementException {
        Optional<Voucher> voucherOptional = repository.findByCode(code);
        if(voucherOptional.isPresent()) {
            Voucher voucher = voucherOptional.get();
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                voucher.addUser(user);
                repository.save(voucher);
                return true;
            } else {
                throw new NoSuchElementException("userId: " + userId + " is not exists");
            }

        } else {
            throw new NoSuchElementException("voucher code: " + code + " is not exists");
        }
    }
    public void attachObserver(Observer observer) {
        observers.add(observer);

    }

    public void detachObserver(Observer observer) {
        observers.remove(observer);
    }

}
