package com.github.makiftutuncu.shoppingcart;

import java.util.*;

public class ShoppingCart {
    private Map<UUID, CartItem> items                  = new HashMap<>();
    private Map<UUID, Category> categories             = new HashMap<>();
    private Map<UUID, Set<CartItem>> itemsInCategories = new HashMap<>();
    private List<Campaign> campaigns                   = new ArrayList<>();
    private List<Coupon> coupons                       = new ArrayList<>();

    private int totalAmount;

    public ShoppingCart addProduct(Product product, int quantity) {
        UUID productId = product.getId();

        CartItem cartItem = items.containsKey(productId) ?
                items.get(productId).withAddedQuantity(quantity) :
                new CartItem(product, quantity);

        items.put(productId, cartItem);
        totalAmount += (product.getPrice() * quantity);

        Category category = product.getCategory();
        UUID categoryId = category.getId();

        categories.put(categoryId, category);

        Set<CartItem> itemsInCurrentCategory = itemsInCategories.containsKey(categoryId) ?
                itemsInCategories.get(categoryId) :
                new HashSet<>();

        itemsInCurrentCategory.add(cartItem);
        itemsInCategories.put(categoryId, itemsInCurrentCategory);

        return this;
    }

    public ShoppingCart applyCampaigns(Campaign campaign, Campaign... otherCampaigns) {
        campaigns.add(campaign);
        campaigns.addAll(Arrays.asList(otherCampaigns));
        return this;
    }

    public ShoppingCart applyCoupons(Coupon coupon, Coupon... otherCoupons) {
        coupons.add(coupon);
        coupons.addAll(Arrays.asList(otherCoupons));
        return this;
    }

    public int numberOfItems() {
        return items.size();
    }

    public int numberOfProducts() {
        return items.values().stream().map(CartItem::getQuantity).reduce(0, Integer::sum);
    }

    public int numberOfDeliveries() {
        return categories.size();
    }

    public void print() {
        int totalCampaignDiscounts = 0;

        for (Category category : categories.values()) {
            System.out.println("================================");
            System.out.println(category.getTitle());
            System.out.println("================================");

            for (CartItem item : itemsInCategories.get(category.getId())) {
                Product product = item.getProduct();

                int campaignDiscount = findBestCampaign(item).map(c -> c.discountFor(product)).orElse(0);

                totalCampaignDiscounts += campaignDiscount;

                System.out.printf("%s x %d\n",  product.getTitle(), item.getQuantity());

                if (item.getQuantity() > 1) {
                    System.out.printf("Total Price: %s\n",  MoneyPrinter.print(item.totalAmount()));
                }

                if (campaignDiscount > 0) {
                    System.out.printf("Unit Price : %s\n",  MoneyPrinter.print(product.getPrice()));
                    System.out.printf("Discount   : -%s\n", MoneyPrinter.print(campaignDiscount));
                }

                System.out.printf("Final Price: %s\n",  MoneyPrinter.print(item.totalAmount() - campaignDiscount));


                System.out.println();
            }
        }

        int cartAmount = totalAmount - totalCampaignDiscounts;

        int couponDiscount = findBestCoupon(cartAmount).map(c -> c.discountFor(cartAmount)).orElse(0);

        int finalAmount = cartAmount - couponDiscount;


        System.out.println("================================");
        if (couponDiscount > 0) {
            System.out.println("Cart Price     : " + MoneyPrinter.print(cartAmount));
            System.out.println("Coupon Discount: -" + MoneyPrinter.print(couponDiscount));
        }

        System.out.println("Final Price    : " + MoneyPrinter.print(finalAmount));
        System.out.println("================================");

    }

    private Optional<Campaign> findBestCampaign(CartItem cartItem) {
        Product product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();
        Category productCategory = product.getCategory();

        return campaigns
                .stream()
                .filter(c -> {
                    Category campaignCategory = c.getCategory();
                    boolean validCategory = campaignCategory.equals(productCategory) || productCategory.isChild(campaignCategory);
                    boolean validQuantity = quantity >= c.getNumberOfItems();

                    return validCategory && validQuantity;
                })
                .max(Comparator.comparingInt(c -> c.discountFor(product)));
    }

    private Optional<Coupon> findBestCoupon(int cartAmount) {
        return coupons
                .stream()
                .filter(c -> c.getMinimumAmount() <= cartAmount)
                .max(Comparator.comparingInt(c -> c.discountFor(cartAmount)));
    }

    @Override public String toString() {
        return String.format("Shopping cart with %d products(s)", numberOfProducts());
    }
}
