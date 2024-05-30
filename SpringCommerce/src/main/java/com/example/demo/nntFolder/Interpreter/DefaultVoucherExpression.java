package com.example.demo.nntFolder.Interpreter;

import java.math.BigDecimal;

public class DefaultVoucherExpression implements Expression{
    private BigDecimal price;

    public DefaultVoucherExpression(BigDecimal price) {
        this.price = price;
    }

    @Override
    public void interpret(Context context) {
        if (context.getRule() == null || context.getRule() == "") {
            context.setTotalPrice(context.getTotalPrice());
        }
    }
}
