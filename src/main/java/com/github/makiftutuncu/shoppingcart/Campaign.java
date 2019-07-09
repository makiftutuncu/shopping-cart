package com.github.makiftutuncu.shoppingcart;

public class Campaign {
    private Category category;
    private int numberOfItems;
    private int amount;
    private double rate;
    private DiscountType discountType;

    private Campaign(Category category, int numberOfItems, DiscountType discountType) {
        setCategory(category);
        setNumberOfItems(numberOfItems);
        setDiscountType(discountType);
    }

    public static Campaign ofAmount(Category category, int numberOfItems, int amount) {
        Campaign campaign = new Campaign(category, numberOfItems, DiscountType.Amount);
        campaign.setAmount(amount);
        return campaign;
    }

    public static Campaign ofRate(Category category, int numberOfItems, double rate) {
        Campaign campaign = new Campaign(category, numberOfItems, DiscountType.Rate);
        campaign.setRate(rate);
        return campaign;
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
            case Rate:   return String.format("%.0f%% off for %d or more %s", rate * 100, numberOfItems, category.title());
            case Amount: return String.format("%s off for %d or more %s", MoneyPrinter.print(amount), numberOfItems, category.title());
            default:     return "Campaign";
        }
    }

    private void setCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("Campaign category cannot be null!");
        this.category = category;
    }

    private void setNumberOfItems(int numberOfItems) {
        if (numberOfItems < 1) throw new IllegalArgumentException("Campaign number of items must be positive!");
        this.numberOfItems = numberOfItems;
    }

    private void setAmount(int amount) {
        if (amount < 1) throw new IllegalArgumentException("Campaign amount must be positive!");
        this.amount = amount;
        this.rate = 0;
    }

    private void setRate(double rate) {
        if (rate <= 0 || rate > 1) throw new IllegalArgumentException("Campaign rate must be a valid positive percentage!");
        this.amount = 0;
        this.rate = rate;
    }

    private void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}
