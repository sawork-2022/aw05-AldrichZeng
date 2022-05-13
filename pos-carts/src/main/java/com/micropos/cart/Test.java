package com.micropos.cart;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        System.out.println(list);
        list.remove("a");
        System.out.println(list);

    }
}
