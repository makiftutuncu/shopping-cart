package com.github.makiftutuncu.shoppingcart;

public class DeliveryCostCalculator {
    private int costPerCategory;
    private int costPerProduct;
    private int fixedCost;

    public DeliveryCostCalculator(int costPerCategory, int costPerProduct, int fixedCost) {
        setCostPerCategory(costPerCategory);
        setCostPerProduct(costPerProduct);
        setFixedCost(fixedCost);
    }

    public int calculateFor(int numberOfCategories, int numberOfItems) {
        return (numberOfCategories * costPerCategory) + (numberOfItems * costPerProduct) + fixedCost;
    }

    private void setCostPerCategory(int costPerCategory) {
        // TODO: Validate
        this.costPerCategory = costPerCategory;
    }

    private void setCostPerProduct(int costPerProduct) {
        // TODO: Validate
        this.costPerProduct = costPerProduct;
    }

    private void setFixedCost(int fixedCost) {
        // TODO: Validate
        this.fixedCost = fixedCost;
    }
}
