package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    private DeliveryCostCalculator calculator = new DeliveryCostCalculator(100, 50, 299);
    private ShoppingCart cart = new ShoppingCart(calculator);

    private Category food  = new Category("Food");
    private Category fruit = new Category("Fruit", food);

    private Product apple  = new Product("Apple", 150, fruit);
    private Product orange = new Product("Orange", 200, fruit);
    private Product milk   = new Product("Milk", 500, food);
    private Product bread  = new Product("Bread", 100, food);
    private Product water  = new Product("Water", 50, food);

    private Campaign oneLiraOffForTwoFoods  = Campaign.ofAmount(food, 2, 100);
    private Campaign tenPercentOffTwoFruits = Campaign.ofRate(fruit, 2, 0.10);

    private Coupon fiveLirasOffCartForTwentyFiveLiras = Coupon.ofAmount(2500, 500);
    private Coupon twoPercentOffCartForTwentyLiras    = Coupon.ofRate(2000, 0.02);

    @BeforeEach
    void reset() {
        cart = new ShoppingCart(calculator);
    }

    void prepareDefaultCart() {
        cart.addProduct(bread, 3)
            .addProduct(milk, 2)
            .addProduct(apple, 4)
            .addProduct(orange, 5)
            .addProduct(water, 1)
            .addCampaigns(oneLiraOffForTwoFoods, tenPercentOffTwoFruits)
            .addCoupons(fiveLirasOffCartForTwentyFiveLiras, twoPercentOffCartForTwentyLiras);
    }

    @Test void failToAddInvalidProduct() {
        assertThrows(IllegalArgumentException.class, () -> cart.addProduct(null, 1));
    }

    @Test void failToAddInvalidQuantity() {
        assertThrows(IllegalArgumentException.class, () -> cart.addProduct(bread, -1));
        assertThrows(IllegalArgumentException.class, () -> cart.addProduct(bread, 0));
    }

    @Test void addProduct() {
        assertEquals(cart.toString(), "Shopping cart with 0 item(s)");

        cart.addProduct(bread, 1);

        assertEquals(cart.checkout().finalAmount(), 100);
        assertEquals(cart.toString(), "Shopping cart with 1 item(s)");

        cart.addProduct(bread, 2);

        assertEquals(cart.checkout().finalAmount(), 300);
        assertEquals(cart.toString(), "Shopping cart with 1 item(s)");

        cart.addProduct(water, 1);

        assertEquals(cart.checkout().finalAmount(), 350);
        assertEquals(cart.toString(), "Shopping cart with 2 item(s)");
    }

    @Test void failToAddInvalidCampaign() {
        assertThrows(IllegalArgumentException.class, () -> cart.addCampaigns(null));
        assertThrows(IllegalArgumentException.class, () -> cart.addCampaigns(oneLiraOffForTwoFoods, (Campaign) null));
        assertThrows(IllegalArgumentException.class, () -> cart.addCampaigns(oneLiraOffForTwoFoods, tenPercentOffTwoFruits, null));
    }

    @Test void addCampaigns() {
        assertEquals(cart.checkout().totalCampaignDiscount(), 0);

        cart.addCampaigns(oneLiraOffForTwoFoods);

        assertEquals(cart.checkout().totalCampaignDiscount(), 0);

        cart.addProduct(milk, 2);

        assertEquals(cart.checkout().totalCampaignDiscount(), 0);

        cart.addProduct(bread, 1);

        assertEquals(cart.checkout().totalCampaignDiscount(), 100);

        cart.addCampaigns(tenPercentOffTwoFruits);

        assertEquals(cart.checkout().totalCampaignDiscount(), 100);

        cart.addProduct(apple, 5);

        assertEquals(cart.checkout().totalCampaignDiscount(), 100);

        cart.addProduct(orange, 5);

        assertEquals(cart.checkout().totalCampaignDiscount(), 275);
    }

    @Test void failToAddInvalidCoupon() {
        assertThrows(IllegalArgumentException.class, () -> cart.addCoupons(null));
        assertThrows(IllegalArgumentException.class, () -> cart.addCoupons(fiveLirasOffCartForTwentyFiveLiras, (Coupon) null));
        assertThrows(IllegalArgumentException.class, () -> cart.addCoupons(fiveLirasOffCartForTwentyFiveLiras, twoPercentOffCartForTwentyLiras, null));
    }

    @Test void addCoupons() {
        assertEquals(cart.checkout().totalCouponDiscount(), 0);

        cart.addCoupons(twoPercentOffCartForTwentyLiras);

        assertEquals(cart.checkout().totalCouponDiscount(), 0);

        cart.addProduct(milk, 2);

        assertEquals(cart.checkout().totalCouponDiscount(), 0);

        cart.addProduct(orange, 5);

        assertEquals(cart.checkout().totalCouponDiscount(), 40);

        cart.addCoupons(fiveLirasOffCartForTwentyFiveLiras);

        assertEquals(cart.checkout().totalCouponDiscount(), 40);

        cart.addProduct(apple, 4);

        assertEquals(cart.checkout().totalCouponDiscount(), 500);
    }

    @Test void checkoutWithDefaultCart() {
        prepareDefaultCart();

        Checkout checkout = cart.checkout();

        assertEquals(checkout.totalAmount(), 2950);
        assertEquals(checkout.totalCampaignDiscount(), 260);
        assertEquals(checkout.totalCouponDiscount(), 500);
        assertEquals(checkout.finalAmount(), 2190);
    }

    @Test void checkoutWithMultipleCampaignsOnSameCategory() {
        Campaign foodCampaign1 = Campaign.ofAmount(food, 1, 100);
        Campaign foodCampaign2 = Campaign.ofRate(food, 1, 0.05);

        cart.addCampaigns(foodCampaign1, foodCampaign2).addProduct(orange, 5).addProduct(milk, 1);

        Checkout checkout = cart.checkout();

        assertEquals(checkout.totalAmount(), 1500);
        assertEquals(checkout.totalCampaignDiscount(), 150);
        assertEquals(checkout.totalCouponDiscount(), 0);
        assertEquals(checkout.finalAmount(), 1350);
    }

    @Test void deliveryCost() {
        prepareDefaultCart();

        assertEquals(cart.deliveryCost(), 749);
    }

    @Test void print() {
        prepareDefaultCart();
        assertDoesNotThrow(() -> cart.print());
    }
}
