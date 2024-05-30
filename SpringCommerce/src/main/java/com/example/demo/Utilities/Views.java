package com.example.demo.Utilities;

public class Views {

    public static class Public {}
    public static class Detail extends HaveCategoty {}
    public static class OrderDetail extends Public {}
    public static class CartProduct extends Public {}


    public static class Private extends Public {}
    public static class HaveCategoty extends Public {}


}

