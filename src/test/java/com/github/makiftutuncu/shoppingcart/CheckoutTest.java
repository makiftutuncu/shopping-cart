package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutTest {
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

    @Test void totalAmount() {
        prepareDefaultCart();

        Checkout checkout = cart.checkout();

        assertEquals(checkout.totalAmount(), 2950);
    }

    @Test void totalCampaignDiscount() {
        prepareDefaultCart();

        Checkout checkout = cart.checkout();

        assertEquals(checkout.totalCampaignDiscount(), 260);
    }

    @Test void totalCouponDiscount() {
        prepareDefaultCart();

        Checkout checkout = cart.checkout();

        assertEquals(checkout.totalCouponDiscount(), 500);
    }

    @Test void finalAmount() {
        prepareDefaultCart();

        Checkout checkout1 = cart.checkout();

        assertEquals(checkout1.finalAmount(), 2190);

        reset();

        Campaign hugeCampaign = Campaign.ofAmount(food, 1, 1000);
        cart.addCampaigns(hugeCampaign).addProduct(bread, 1);
        Checkout checkout2 = cart.checkout();

        assertEquals(checkout2.finalAmount(), 0);
    }

    @Test void campaignsByCategory() {
        prepareDefaultCart();

        Checkout checkout = cart.checkout();
        Map<Category, Campaign> campaignsByCategory = checkout.campaignsByCategory();

        assertEquals(campaignsByCategory.get(food), oneLiraOffForTwoFoods);
        assertEquals(campaignsByCategory.get(fruit), tenPercentOffTwoFruits);
    }

    @Test void campaignDiscountsByCategory() {
        prepareDefaultCart();

        Checkout checkout = cart.checkout();
        Map<Category, Integer> campaignDiscountsByCategory = checkout.campaignDiscountsByCategory();

        assertEquals(campaignDiscountsByCategory.get(food), 100);
        assertEquals(campaignDiscountsByCategory.get(fruit), 160);
    }

    @Test void appliedCoupon() {
        Checkout checkout1 = cart.checkout();

        assertFalse(checkout1.appliedCoupon().isPresent());

        prepareDefaultCart();

        Checkout checkout2 = cart.checkout();

        assertTrue(checkout2.appliedCoupon().isPresent());

        Coupon coupon = checkout2.appliedCoupon().get();

        assertEquals(coupon, fiveLirasOffCartForTwentyFiveLiras);
    }

    @Test void calculateEmptyCart() {
        Checkout checkout = cart.checkout();

        assertEquals(checkout.totalAmount(), 0);
        assertEquals(checkout.totalCampaignDiscount(), 0);
        assertEquals(checkout.totalCouponDiscount(), 0);
        assertEquals(checkout.finalAmount(), 0);
        assertTrue(checkout.campaignsByCategory().isEmpty());
        assertTrue(checkout.campaignDiscountsByCategory().isEmpty());
        assertFalse(checkout.appliedCoupon().isPresent());
    }

    @Test void calculateDefaultCart() {
        prepareDefaultCart();

        Checkout checkout = cart.checkout();

        assertEquals(checkout.totalAmount(), 2950);
        assertEquals(checkout.totalCampaignDiscount(), 260);
        assertEquals(checkout.totalCouponDiscount(), 500);
        assertEquals(checkout.finalAmount(), 2190);

        assertFalse(checkout.campaignsByCategory().isEmpty());
        Map<Category, Campaign> campaignsByCategory = checkout.campaignsByCategory();

        assertEquals(campaignsByCategory.get(food), oneLiraOffForTwoFoods);
        assertEquals(campaignsByCategory.get(fruit), tenPercentOffTwoFruits);

        assertFalse(checkout.campaignDiscountsByCategory().isEmpty());

        Map<Category, Integer> campaignDiscountsByCategory = checkout.campaignDiscountsByCategory();

        assertEquals(campaignDiscountsByCategory.get(food), 100);
        assertEquals(campaignDiscountsByCategory.get(fruit), 160);

        assertTrue(checkout.appliedCoupon().isPresent());

        Coupon coupon = checkout.appliedCoupon().get();

        assertEquals(coupon, fiveLirasOffCartForTwentyFiveLiras);
    }
}
