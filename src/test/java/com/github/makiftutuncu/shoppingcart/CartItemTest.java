package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartItemTest {
    private Category electronics = new Category("Electronics");
    private Product iPhone = new Product("iPhone Xs Max", 1000000, electronics);

    @Test void failToCreateCartItemWithInvalidProduct() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(null, 1));
    }

    @Test void failToCreateCartItemWithInvalidQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(iPhone, -1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem(iPhone, 0));
    }

    @Test void createCartItem() {
        CartItem cartItem = new CartItem(iPhone, 2);
        assertEquals(cartItem.product(), iPhone);
        assertEquals(cartItem.quantity(), 2);
        assertEquals(cartItem.toString(), "iPhone Xs Max x 2 = ₺20000.00");
    }

    @Test void updateQuantityByAdding() {
        CartItem cartItem1 = new CartItem(iPhone, 1);
        CartItem cartItem2 = cartItem1.addingQuantity(2);

        assertEquals(cartItem1.quantity(), 3);
        assertEquals(cartItem2.quantity(), 3);
    }

    @Test void calculateTotalAmount() {
        CartItem cartItem = new CartItem(iPhone, 3);
        assertEquals(cartItem.totalAmount(), 3000000);
    }
}
