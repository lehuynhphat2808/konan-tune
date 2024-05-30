package com.example.demo.nntFolder.Interpreter;

public class BuyMoreThanXExpression implements Expression{
    @Override
    public void interpret(Context context) {
        int x = parseBuyMoreThanX(context.getRule());
        if(x > 0){
            context.setTotalPrice(context.getTotalPrice().subtract(context.getDiscount()));
        }
    }

    public static int parseBuyMoreThanX(String input) {
        if (input.startsWith("BUY MORE THAN ")) {
            String numberString = input.substring(14);
            try {
                int value = Integer.parseInt(numberString);
                return value;
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }


}
