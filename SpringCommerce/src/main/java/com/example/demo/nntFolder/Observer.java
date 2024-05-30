package com.example.demo.nntFolder;


import java.util.UUID;

public interface Observer {
    public void onVoucherApply(UUID userId, String voucherCode);
}