package com.github.makiftutuncu.shoppingcart;

public class MoneyPrinter {
    private final static String CURRENCY = "₺";
    private final static boolean PREFIX  = true;

    public static String print(int amount) {
        return print(amount, CURRENCY, PREFIX);
    }

    public static String print(int amount, String currency, boolean currencyAsPrefix) {
        String sign           = amount < 0 ? "-" : "";
        String currencyPrefix = currencyAsPrefix ? currency : "";
        String money          = String.format("%.2f", Math.abs(((double) amount) / 100));
        String currencySuffix = currencyAsPrefix ? "" : currency;

        return sign + currencyPrefix + money + currencySuffix;
    }
}
