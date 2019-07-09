package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CouponTest {
    @Test void failToCreateCouponWithInvalidMinimumAmount() {
        assertThrows(IllegalArgumentException.class, () -> Coupon.ofAmount(-1, 100));
        assertThrows(IllegalArgumentException.class, () -> Coupon.ofAmount(-0, 100));
    }

    @Test void failToCreateCouponWithInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> Coupon.ofAmount(1000, -100));
        assertThrows(IllegalArgumentException.class, () -> Coupon.ofAmount(1000, 0));
    }

    @Test void failToCreateCouponWithInvalidRate() {
        assertThrows(IllegalArgumentException.class, () -> Coupon.ofRate(1000, -0.5));
        assertThrows(IllegalArgumentException.class, () -> Coupon.ofRate(1000, 0));
        assertThrows(IllegalArgumentException.class, () -> Coupon.ofRate(1000, 1.5));
    }

    @Test void createCouponWithAmount() {
        Coupon coupon = Coupon.ofAmount(1000, 100);
        assertEquals(coupon.minimumAmount(), 1000);
        assertEquals(coupon.toString(), "₺1.00 off on the cart for ₺10.00 or more");
    }

    @Test void calculateDiscountBasedOnAmount() {
        Coupon coupon = Coupon.ofAmount(1000, 100);
        assertEquals(coupon.discountFor(500), 0);
        assertEquals(coupon.discountFor(1000), 100);
        assertEquals(coupon.discountFor(1500), 100);
    }

    @Test void createCouponWithRate() {
        Coupon coupon = Coupon.ofRate(1000, 0.20);
        assertEquals(coupon.minimumAmount(), 1000);
        assertEquals(coupon.toString(), "20% off on the cart for ₺10.00 or more");
    }

    @Test void calculateDiscountBasedOnRate() {
        Coupon coupon = Coupon.ofRate(1000, 0.20);
        assertEquals(coupon.discountFor(500), 0);
        assertEquals(coupon.discountFor(1000), 200);
        assertEquals(coupon.discountFor(1500), 300);
    }
}
