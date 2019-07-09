package com.github.makiftutuncu.shoppingcart;

public class MoneyPrinter {
    public static String print(int amount) {
        return String.format("₺%.2f", ((double) amount) / 100);
    }
}
