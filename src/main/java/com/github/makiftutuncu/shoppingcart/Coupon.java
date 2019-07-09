package com.github.makiftutuncu.shoppingcart;

public class Coupon {
    private int minimumAmount;
    private int amount;
    private double rate;
    private DiscountType discountType;

    private Coupon(int minimumAmount, int amount, double rate, DiscountType discountType) {
        setMinimumAmount(minimumAmount);
        setAmount(amount);
        setRate(rate);
        setDiscountType(discountType);
    }

    public static Coupon ofAmount(int minimumAmount, int amount) {
        return new Coupon(minimumAmount, amount, 0.0, DiscountType.Amount);
    }

    public static Coupon ofRate(int minimumAmount, double rate) {
        return new Coupon(minimumAmount, 0, rate, DiscountType.Rate);
    }

    public int minimumAmount() {
        return minimumAmount;
    }

    public int discountFor(int cartAmount) {
        switch (discountType) {
            case Rate:   return ((int) (cartAmount * rate));
            case Amount: return amount;
            default:     return 0;
        }
    }

    @Override public String toString() {
        switch (discountType) {
            case Rate:   return String.format("%.0f%% off on the cart for %s or more", rate * 100, MoneyPrinter.print(minimumAmount));
            case Amount: return String.format("%s off on the cart for %s or more", MoneyPrinter.print(amount), MoneyPrinter.print(minimumAmount));
            default:     return "Coupon";
        }
    }

    private void setMinimumAmount(int minimumAmount) {
        // TODO: Validate
        this.minimumAmount = minimumAmount;
    }

    private void setAmount(int amount) {
        // TODO: Validate
        this.amount = amount;
    }

    private void setRate(double rate) {
        // TODO: Validate
        this.rate = rate;
    }

    private void setDiscountType(DiscountType discountType) {
        // TODO: Validate
        this.discountType = discountType;
    }
}
