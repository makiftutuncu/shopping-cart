package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {
    private Category electronics = new Category("Electronics");

    @Test void failToCreateProductWithInvalidTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Product(null, 1500000, electronics));
        assertThrows(IllegalArgumentException.class, () -> new Product("", 1500000, electronics));
        assertThrows(IllegalArgumentException.class, () -> new Product("   ", 1500000, electronics));
    }

    @Test void failToCreateProductWithInvalidPrice() {
        assertThrows(IllegalArgumentException.class, () -> new Product("MacBook Pro", -1, electronics));
        assertThrows(IllegalArgumentException.class, () -> new Product("MacBook Pro", 0, electronics));
    }

    @Test void failToCreateProductWithInvalidCategory() {
        assertThrows(IllegalArgumentException.class, () -> new Product("MacBook Pro", 1500000, null));
    }

    @Test void createProduct() {
        Product product = new Product("MacBook Pro", 1500000, electronics);

        assertEquals(product.title(), "MacBook Pro");
        assertEquals(product.price(), 1500000);
        assertEquals(product.price(), 1500000);
        assertEquals(product.category(), electronics);
        assertEquals(product.toString(), "MacBook Pro (₺15000.00)");
    }
}
