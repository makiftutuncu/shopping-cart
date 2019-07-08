package com.github.makiftutuncu.shoppingcart;

public class Campaign {
    private Category category;
    private int numberOfItems;
    private int amount;
    private double rate;
    private DiscountType discountType;

    private Campaign(Category category, int numberOfItems, int amount, double rate, DiscountType discountType) {
        setCategory(category);
        setNumberOfItems(numberOfItems);
        setAmount(amount);
        setRate(rate);
        setDiscountType(discountType);
    }

    public static Campaign ofAmount(Category category, int numberOfItems, int amount) {
        return new Campaign(category, numberOfItems, amount, 0.0, DiscountType.Amount);
    }

    public static Campaign ofRate(Category category, int numberOfItems, double rate) {
        return new Campaign(category, numberOfItems, 0, rate, DiscountType.Rate);
    }

    public Category category() {
        return category;
    }

    public int numberOfItems() {
        return numberOfItems;
    }

    public int discountFor(int totalAmount) {
        switch (discountType) {
            case Rate:   return ((int) (totalAmount * rate));
            case Amount: return amount;
            default:     return 0;
        }
    }

    @Override public String toString() {
        switch (discountType) {
            case Rate:   return String.format("%.0f%% for %d or more %s", rate * 100, numberOfItems, category.title());
            case Amount: return String.format("%s off for %d or more %s", MoneyPrinter.print(amount), numberOfItems, category.title());
            default:     return "Campaign";
        }
    }

    private void setCategory(Category category) {
        // TODO: Validate
        this.category = category;
    }

    private void setNumberOfItems(int numberOfItems) {
        // TODO: Validate
        this.numberOfItems = numberOfItems;
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
