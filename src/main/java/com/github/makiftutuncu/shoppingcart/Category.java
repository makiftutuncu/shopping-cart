package com.github.makiftutuncu.shoppingcart;

import java.util.Objects;
import java.util.Optional;

/**
 * Category represents a group of related {@link com.github.makiftutuncu.shoppingcart.Product}s
 */
public class Category {
    private String title;
    private Category parent;

    /**
     * Creates a category with a parent
     *
     * @param title  Title of the category, cannot be empty or null
     * @param parent Parent of the category, can be null
     */
    public Category(String title, Category parent) {
        setTitle(title);
        setParent(parent);
    }

    /**
     * Creates a category without a parent
     *
     * @param title Title of the category, cannot be empty or null
     */
    public Category(String title) {
        this(title, null);
    }

    /**
     * @return Title of the category
     */
    public String title() {
        return title;
    }

    /**
     * @return Parent of the category optionally or `Optional.empty()` if category has no parent
     */
    public Optional<Category> parent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Recursively checks if this category is a child of given category
     *
     * @param parent Category to check against
     *
     * @return true if this category is child of given category (or one of it's parent's), false otherwise
     */
    public boolean isChild(Category parent) {
        if (this.parent == null) return false;
        if (parent == null)      return false;

        return this.parent.equals(parent) || this.parent.isChild(parent);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category c = (Category) o;

        return title.equals(c.title) && parent().equals(c.parent());
    }

    @Override public int hashCode() {
        return Objects.hash(title, parent);
    }

    @Override public String toString() {
        return parent().map(parent -> parent.toString() + " / ").orElse("") + title;
    }

    private void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Category title cannot be null or empty!");
        this.title = title;
    }

    private void setParent(Category parent) {
        this.parent = parent;
    }
}
