package com.github.makiftutuncu.shoppingcart;

public class Main {
    public static void main(String[] args) {
        DeliveryCostCalculator calculator = new DeliveryCostCalculator(100, 50);
        ShoppingCart cart = new ShoppingCart(calculator);

        Category food  = new Category("Food");
        Category fruit = new Category("Fruit", food);

        Product apple  = new Product("Apple", 150, fruit);
        Product orange = new Product("Orange", 200, fruit);
        Product milk   = new Product("Milk", 500, food);
        Product bread  = new Product("Bread", 100, food);
        Product water  = new Product("Water", 50, food);

        Campaign oneLiraOffForTwoFoods    = Campaign.ofAmount(food, 2, 100);
        Campaign tenPercentOffThreeFruits = Campaign.ofRate(fruit, 3, 0.1);

        Coupon fiveLirasOffCartForTwentyFiveLiras = Coupon.ofAmount(2500, 500);
        Coupon twoPercentOffCartForTwentyLiras   = Coupon.ofRate(2000, 0.02);

        cart.addProduct(bread, 3)
            .addProduct(milk, 2)
            .addProduct(apple, 4)
            .addProduct(orange, 5)
            .addProduct(water, 1)
            .applyCampaigns(oneLiraOffForTwoFoods, tenPercentOffThreeFruits)
            .applyCoupons(fiveLirasOffCartForTwentyFiveLiras, twoPercentOffCartForTwentyLiras);

        cart.print();
    }
}
