package com.github.makiftutuncu.shoppingcart;

import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCart {
    private Map<UUID, CartItem> items                  = new HashMap<>();
    private Map<Category, List<CartItem>> groupedItems = new HashMap<>();
    private List<Campaign> campaigns                   = new ArrayList<>();
    private List<Coupon> coupons                       = new ArrayList<>();

    private DeliveryCostCalculator deliveryCostCalculator;

    public ShoppingCart(DeliveryCostCalculator deliveryCostCalculator) {
        this.deliveryCostCalculator = deliveryCostCalculator;
    }

    public ShoppingCart addProduct(Product product, int quantity) {
        UUID productId = product.id();

        CartItem cartItem = items.containsKey(productId) ?
                items.get(productId).addingQuantity(quantity) :
                new CartItem(product, quantity);

        items.put(productId, cartItem);

        Category category = product.category();
        List<CartItem> items = groupedItems.getOrDefault(category, new ArrayList<>());
        items.add(cartItem);
        groupedItems.put(category, items);

        return this;
    }

    public ShoppingCart addCampaigns(Campaign campaign, Campaign... otherCampaigns) {
        campaigns.add(campaign);
        campaigns.addAll(Arrays.asList(otherCampaigns));
        return this;
    }

    public ShoppingCart addCoupons(Coupon coupon, Coupon... otherCoupons) {
        coupons.add(coupon);
        coupons.addAll(Arrays.asList(otherCoupons));
        return this;
    }

    public void print() {
        int totalAmount = 0;
        int totalCampaignDiscount = 0;

        for (Map.Entry<Category, List<CartItem>> entry : groupedItems.entrySet()) {
            Category category              = entry.getKey();
            List<CartItem> itemsInCategory = entry.getValue();

            int temporaryTotalAmountForCategory = 0;

            System.out.println("================================");
            System.out.println(category.title());
            System.out.println("================================");

            for (CartItem item : itemsInCategory) {
                temporaryTotalAmountForCategory += item.totalAmount();

                Product product = item.product();

                System.out.printf("%s x %d\n",  product.title(), item.quantity());

                if (item.quantity() > 1) {
                    System.out.printf("Unit Amount : %s\n",  MoneyPrinter.print(product.price()));
                    System.out.printf("Total Amount: %s\n",  MoneyPrinter.print(item.totalAmount()));
                } else {
                    System.out.printf("Amount: %s\n",  MoneyPrinter.print(item.totalAmount()));
                }

                System.out.println();
            }

            final int totalAmountForCategory = temporaryTotalAmountForCategory;

            totalAmount += totalAmountForCategory;

            int campaignDiscount = findBestCampaignFor(category)
                    .map(campaign -> {
                        int discount = campaign.discountFor(totalAmountForCategory);
                        System.out.printf("Applied Campaign: %s\n", campaign.toString());
                        System.out.printf("Discount        : -%s\n\n", MoneyPrinter.print(discount));
                        return discount;
                    })
                    .orElse(0);

            totalCampaignDiscount += campaignDiscount;
        }

        int cartAmount   = totalAmount - totalCampaignDiscount;
        int deliveryCost = deliveryCost();

        System.out.println("================================");
        System.out.println("Total Amount      :  " + MoneyPrinter.print(totalAmount));
        System.out.println("Delivery Cost     :  " + MoneyPrinter.print(deliveryCost));

        if (totalCampaignDiscount > 0) {
            System.out.println("Campaign Discounts: -" + MoneyPrinter.print(totalCampaignDiscount));
        }

        int couponDiscount = findBestCoupon(cartAmount).map(coupon -> {
            int discount = coupon.discountFor(cartAmount);
            System.out.println();
            System.out.printf("Applied Coupon: %s\n", coupon.toString());
            System.out.printf("Discount      : -%s\n", MoneyPrinter.print(discount));
            return discount;
        }).orElse(0);

        int finalAmount = cartAmount - couponDiscount + deliveryCost;

        System.out.println();
        System.out.println("Final Amount: " + MoneyPrinter.print(finalAmount));
        System.out.println("================================");
    }

    @Override public String toString() {
        return String.format("Shopping cart with %d item(s)", items.size());
    }

    private int deliveryCost() {
        return deliveryCostCalculator.calculateFor(groupedItems.keySet().size(), items.size());
    }

    private Optional<Campaign> findBestCampaignFor(Category category) {
        List<CartItem> itemsInCategory =
            items.values()
                 .stream()
                 .filter(item -> item.product().category().isChild(category) || item.product().category().equals(category))
                 .collect(Collectors.toList());

        int numberOfItemsInCategory = itemsInCategory.size();
        int totalAmountForCategory  = itemsInCategory.stream().map(CartItem::totalAmount).reduce(0, Integer::sum);

        return campaigns.stream()
                .filter(campaign -> campaign.category().equals(category) && numberOfItemsInCategory >= campaign.numberOfItems())
                .max(Comparator.comparingInt(c -> c.discountFor(totalAmountForCategory)));
    }

    private Optional<Coupon> findBestCoupon(int cartAmount) {
        return coupons.stream()
                      .filter(c -> c.minimumAmount() <= cartAmount)
                      .max(Comparator.comparingInt(c -> c.discountFor(cartAmount)));
    }
}
