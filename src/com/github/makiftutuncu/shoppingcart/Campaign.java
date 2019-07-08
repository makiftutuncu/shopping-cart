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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        // TODO: Validate
        this.category = category;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        // TODO: Validate
        this.numberOfItems = numberOfItems;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        // TODO: Validate
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        // TODO: Validate
        this.rate = rate;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        // TODO: Validate
        this.discountType = discountType;
    }

    public int discountFor(Product product) {
        switch (discountType) {
            case Rate:   return ((int) (product.getPrice() * rate));
            case Amount: return amount;
            default:     return 0;
        }
    }

    @Override public String toString() {
        switch (discountType) {
            case Rate:   return String.format("%.0f%% off for %d or more products in %s", rate * 100, numberOfItems, category.getTitle());
            case Amount: return String.format("%s off for %d or more products in %s", MoneyPrinter.print(amount), numberOfItems, category.getTitle());
            default:     return "Campaign";
        }
    }
}
