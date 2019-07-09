package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {
    @Test void failToCreateCategoryWithInvalidTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Category(null));
        assertThrows(IllegalArgumentException.class, () -> new Category(""));
        assertThrows(IllegalArgumentException.class, () -> new Category("  "));
    }

    @Test void createCategoryWithNoParent() {
        Category category = new Category("Electronics");

        assertEquals(category.title(), "Electronics");
        assertEquals(category.parent(), Optional.empty());
        assertEquals(category.toString(), "Electronics");
    }

    @Test void createCategoryWithParent() {
        Category electronics = new Category("Electronics");
        Category mobilePhones = new Category("Mobile Phones", electronics);
        Category android = new Category("Android", mobilePhones);

        assertEquals(android.title(), "Android");
        assertEquals(android.parent(), Optional.of(mobilePhones));
        assertEquals(android.toString(), "Electronics / Mobile Phones / Android");
    }

    @Test void checkIfChildCategory() {
        Category electronics = new Category("Electronics");
        Category mobilePhones = new Category("Mobile Phones", electronics);
        Category android = new Category("Android", mobilePhones);

        assertFalse(electronics.isChild(electronics));
        assertTrue(mobilePhones.isChild(electronics));
        assertTrue(android.isChild(mobilePhones));
        assertTrue(android.isChild(electronics));
        assertFalse(mobilePhones.isChild(android));
    }
}
