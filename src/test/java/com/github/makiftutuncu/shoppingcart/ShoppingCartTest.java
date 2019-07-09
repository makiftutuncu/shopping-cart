package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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

        assertEquals(cart.totalAmountAfterDiscounts(), 100);
        assertEquals(cart.toString(), "Shopping cart with 1 item(s)");

        cart.addProduct(bread, 2);

        assertEquals(cart.totalAmountAfterDiscounts(), 300);
        assertEquals(cart.toString(), "Shopping cart with 1 item(s)");

        cart.addProduct(water, 1);

        assertEquals(cart.totalAmountAfterDiscounts(), 350);
        assertEquals(cart.toString(), "Shopping cart with 2 item(s)");
    }

    @Test void failToAddInvalidCampaign() {
        assertThrows(IllegalArgumentException.class, () -> cart.addCampaigns(null));
        assertThrows(IllegalArgumentException.class, () -> cart.addCampaigns(oneLiraOffForTwoFoods, (Campaign) null));
        assertThrows(IllegalArgumentException.class, () -> cart.addCampaigns(oneLiraOffForTwoFoods, tenPercentOffTwoFruits, null));
    }

    @Test void addCampaigns() {
        assertEquals(cart.campaignDiscount(), 0);

        cart.addCampaigns(oneLiraOffForTwoFoods);

        assertEquals(cart.campaignDiscount(), 0);

        cart.addProduct(milk, 2);

        assertEquals(cart.campaignDiscount(), 0);

        cart.addProduct(bread, 1);

        assertEquals(cart.campaignDiscount(), 100);

        cart.addCampaigns(tenPercentOffTwoFruits);

        assertEquals(cart.campaignDiscount(), 100);

        cart.addProduct(apple, 5);

        assertEquals(cart.campaignDiscount(), 100);

        cart.addProduct(orange, 5);

        assertEquals(cart.campaignDiscount(), 275);
    }

    @Test void failToAddInvalidCoupon() {
        assertThrows(IllegalArgumentException.class, () -> cart.addCoupons(null));
        assertThrows(IllegalArgumentException.class, () -> cart.addCoupons(fiveLirasOffCartForTwentyFiveLiras, (Coupon) null));
        assertThrows(IllegalArgumentException.class, () -> cart.addCoupons(fiveLirasOffCartForTwentyFiveLiras, twoPercentOffCartForTwentyLiras, null));
    }

    @Test void addCoupons() {
        assertEquals(cart.couponDiscount(), 0);

        cart.addCoupons(twoPercentOffCartForTwentyLiras);

        assertEquals(cart.couponDiscount(), 0);

        cart.addProduct(milk, 2);

        assertEquals(cart.couponDiscount(), 0);

        cart.addProduct(orange, 5);

        assertEquals(cart.couponDiscount(), 40);

        cart.addCoupons(fiveLirasOffCartForTwentyFiveLiras);

        assertEquals(cart.couponDiscount(), 40);

        cart.addProduct(apple, 4);

        assertEquals(cart.couponDiscount(), 500);
    }

    @Test void calculateAmounts() {
        prepareDefaultCart();

        int[] amounts = cart.calculateAmounts();

        assertEquals(amounts[0], 2950);
        assertEquals(amounts[1], 260);
        assertEquals(amounts[2], 500);
    }

    @Test void campaignDiscount() {
        prepareDefaultCart();

        assertEquals(cart.campaignDiscount(), 260);
    }

    @Test void couponDiscount() {
        prepareDefaultCart();

        assertEquals(cart.couponDiscount(), 500);
    }

    @Test void totalAmountAfterDiscounts() {
        prepareDefaultCart();

        assertEquals(cart.totalAmountAfterDiscounts(), 2190);
    }

    @Test void deliveryCost() {
        prepareDefaultCart();

        assertEquals(cart.deliveryCost(), 749);
    }

    @Test void findBestCampaign() {
        prepareDefaultCart();

        Optional<Campaign> maybeFruitCampaign = cart.findBestCampaignFor(fruit);
        assertTrue(maybeFruitCampaign.isPresent());
        assertEquals(maybeFruitCampaign.get(), tenPercentOffTwoFruits);

        reset();

        assertFalse(cart.findBestCampaignFor(fruit).isPresent());

        cart.addProduct(bread, 3)
            .addProduct(apple, 4)
            .addCampaigns(oneLiraOffForTwoFoods, tenPercentOffTwoFruits);

        assertFalse(cart.findBestCampaignFor(fruit).isPresent());
    }

    @Test void findBestCoupon() {
        prepareDefaultCart();

        assertFalse(cart.findBestCoupon(1000).isPresent());

        Optional<Coupon> maybeCoupon1 = cart.findBestCoupon(2000);
        assertTrue(maybeCoupon1.isPresent());
        assertEquals(maybeCoupon1.get(), twoPercentOffCartForTwentyLiras);

        Optional<Coupon> maybeCoupon2 = cart.findBestCoupon(3000);
        assertTrue(maybeCoupon2.isPresent());
        assertEquals(maybeCoupon2.get(), fiveLirasOffCartForTwentyFiveLiras);
    }

    @Test void print() {
        prepareDefaultCart();
        assertDoesNotThrow(() -> cart.print());
    }
}
