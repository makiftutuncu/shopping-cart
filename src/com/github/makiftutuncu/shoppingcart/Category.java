package com.github.makiftutuncu.shoppingcart;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Category {
    private UUID id;
    private String title;
    private Category parent;

    public Category(String title, Category parent) {
        this.id = UUID.randomUUID();
        setTitle(title);
        setParent(parent);
    }

    public Category(String title) {
        this(title, null);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // TODO: Validate
        this.title = title;
    }

    public Optional<Category> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public boolean isChild(Category parent) {
        return getParent().map(p -> p.equals(parent)).orElse(false);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category c = (Category) o;

        return id.equals(c.id) && title.equals(c.title) && getParent().equals(c.getParent());
    }

    @Override public int hashCode() {
        return Objects.hash(id, title, parent);
    }

    @Override public String toString() {
        return getParent().map(parent -> parent.toString() + " / ").orElse("") + title;
    }
}
