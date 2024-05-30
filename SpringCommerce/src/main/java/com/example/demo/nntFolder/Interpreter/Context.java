package com.example.demo.nntFolder.Interpreter;

import java.math.BigDecimal;

public class Context {
    private int amount;
    private BigDecimal totalPrice;
    private String rule;
    private BigDecimal discount;

    public Context() {
    }

    public void setDiscount(BigDecimal discount){
        this.discount = discount;
    }
    public BigDecimal getDiscount(){
        return this.discount;
    }
    public Context(int amount, BigDecimal totalPrice, String rule) {
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.rule = rule;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}