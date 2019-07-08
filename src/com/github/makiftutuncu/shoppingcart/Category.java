package com.github.makiftutuncu.shoppingcart;

import java.util.Objects;
import java.util.Optional;

public class Category {
    private String title;
    private Category parent;

    public Category(String title, Category parent) {
        setTitle(title);
        setParent(parent);
    }

    public Category(String title) {
        this(title, null);
    }

    public String title() {
        return title;
    }

    public Optional<Category> parent() {
        return Optional.ofNullable(parent);
    }

    public boolean isChild(Category parent) {
        return parent().map(p -> p.equals(parent)).orElse(false);
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
        // TODO: Validate
        this.title = title;
    }

    private void setParent(Category parent) {
        this.parent = parent;
    }
}
