package com.example.demo.nntFolder.Interpreter;

public class BuyFromXProductExpression implements Expression{
    @Override
    public void interpret(Context context) {
        int x = checkStringFormat(context.getRule());
        if(x>0) {
            context.setTotalPrice(context.getTotalPrice().subtract(context.getDiscount()));
        }

    }

    public static int checkStringFormat(String input) {
        String[] words = input.split(" ");
        if (words.length != 4) {
            return -1;
        }

        if (!words[0].equals("MORE") || !words[1].equals("THAN") || !words[3].equals("PRODUCT")) {
            return -1;
        }
        int x;
        try {
            x = Integer.parseInt(words[2]);
        } catch (NumberFormatException e) {
            return -1;
        }

        return x;
    }


}
