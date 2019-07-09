package com.github.makiftutuncu.shoppingcart;

/**
 * Coupon is a way to make discounts on the {@link com.github.makiftutuncu.shoppingcart.ShoppingCart} itself.
 * It is valid for a specific minimum cart amount.
 * Coupon discounts are either fixed amounts or rates, see {@link com.github.makiftutuncu.shoppingcart.DiscountType}
 */
public class Coupon {
    private int minimumAmount;
    private int amount;
    private double rate;
    private DiscountType discountType;

    private Coupon(int minimumAmount, DiscountType discountType) {
        setMinimumAmount(minimumAmount);
        setDiscountType(discountType);
    }

    /**
     * Creates an amount-based coupon
     *
     * @param minimumAmount Minimum cart amount for the coupon, must be positive
     * @param amount        Amount of discount of the coupon, must be positive
     *
     * @return Created coupon
     */
    public static Coupon ofAmount(int minimumAmount, int amount) {
        Coupon coupon = new Coupon(minimumAmount, DiscountType.Amount);
        coupon.setAmount(amount);
        return coupon;
    }

    /**
     * Creates a rate-based coupon
     *
     * @param minimumAmount Minimum cart amount for the coupon, must be positive
     * @param rate          Rate of discount of the coupon, must be in (0, 1)
     *
     * @return Created coupon
     */
    public static Coupon ofRate(int minimumAmount, double rate) {
        Coupon coupon = new Coupon(minimumAmount, DiscountType.Rate);
        coupon.setRate(rate);
        return coupon;
    }

    /**
     * @return Minimum amount of the coupon
     */
    public int minimumAmount() {
        return minimumAmount;
    }

    /**
     * Calculates the discount amount to be applied
     *
     * @param cartAmount Amount of the cart
     *
     * @return Calculated amount or 0 if there won't be a discount
     */
    public int discountFor(int cartAmount) {
        if (cartAmount < minimumAmount) return 0;

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
        if (minimumAmount < 1) throw new IllegalArgumentException("Coupon minimum amount must be positive!");
        this.minimumAmount = minimumAmount;
    }

    private void setAmount(int amount) {
        if (amount < 1) throw new IllegalArgumentException("Coupon amount must be positive!");
        this.amount = amount;
        this.rate = 0;
    }

    private void setRate(double rate) {
        if (rate <= 0 || rate > 1) throw new IllegalArgumentException("Coupon rate must be a valid positive percentage!");
        this.amount = 0;
        this.rate = rate;
    }

    private void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}
