package com.github.makiftutuncu.shoppingcart;

/**
 * A utility class to format money values properly
 */
public class MoneyPrinter {
    private final static String DEFAULT_CURRENCY = "₺";
    private final static boolean DEFAULT_PREFIX  = true;

    /**
     * Prints given money amount using default currency and currency position
     *
     * @param amount Amount of money to print
     *
     * @return Printed money
     */
    public static String print(int amount) {
        return print(amount, DEFAULT_CURRENCY, DEFAULT_PREFIX);
    }

    /**
     * Prints given money amount using given currency and currency position
     *
     * @param amount           Amount of money to print
     * @param currency         Currency symbol of the money
     * @param currencyAsPrefix Whether or not to print the currency before the amount
     *
     * @return Printed money
     */
    public static String print(int amount, String currency, boolean currencyAsPrefix) {
        String sign           = amount < 0 ? "-" : "";
        String currencyPrefix = currencyAsPrefix ? currency : "";
        String money          = String.format("%.2f", Math.abs(((double) amount) / 100));
        String currencySuffix = currencyAsPrefix ? "" : currency;

        return sign + currencyPrefix + money + currencySuffix;
    }
}
