package com.github.makiftutuncu.shoppingcart;

import java.util.*;

/**
 * Represents a shopping cart to which {@link com.github.makiftutuncu.shoppingcart.Product}s,
 * {@link com.github.makiftutuncu.shoppingcart.Campaign}s, {@link com.github.makiftutuncu.shoppingcart.Coupon}s
 * can be added.
 */
public class ShoppingCart {
    private Map<UUID, CartItem> items                  = new HashMap<>();
    private Map<Category, List<CartItem>> groupedItems = new HashMap<>();
    private List<Campaign> campaigns                   = new ArrayList<>();
    private List<Coupon> coupons                       = new ArrayList<>();

    private DeliveryCostCalculator deliveryCostCalculator;

    /**
     * Creates a shopping cart
     *
     * @param deliveryCostCalculator {@link com.github.makiftutuncu.shoppingcart.DeliveryCostCalculator} to calculate delivery costs
     */
    public ShoppingCart(DeliveryCostCalculator deliveryCostCalculator) {
        this.deliveryCostCalculator = deliveryCostCalculator;
    }

    /**
     * Adds a product of given quantity to the cart, if it is added before, increases the quantity
     *
     * @param product  Product to add, cannot be null
     * @param quantity Quantity to add, must be positive
     *
     * @return Shopping cart itself after adding for chaining
     */
    public ShoppingCart addProduct(Product product, int quantity) {
        CartItem newCartItem = new CartItem(product, quantity);

        UUID productId = product.id();

        CartItem cartItem = items.containsKey(productId) ?
                items.get(productId).addingQuantity(quantity) :
                newCartItem;

        items.put(productId, cartItem);

        Category category = product.category();
        List<CartItem> items = groupedItems.getOrDefault(category, new ArrayList<>());
        if (!items.contains(cartItem)) {
            items.add(cartItem);
        }
        groupedItems.put(category, items);

        return this;
    }

    /**
     * Adds one or more campaigns to the cart
     *
     * @param campaign       A campaign to add, cannot be null
     * @param otherCampaigns Variable number of other campaigns to add, cannot be null or contain null campaigns
     *
     * @return Shopping cart itself after adding for chaining
     */
    public ShoppingCart addCampaigns(Campaign campaign, Campaign... otherCampaigns) {
        if (campaign == null || otherCampaigns == null || Arrays.stream(otherCampaigns).anyMatch(Objects::isNull)) throw new IllegalArgumentException("Shopping cart campaigns cannot be null!");
        campaigns.add(campaign);
        campaigns.addAll(Arrays.asList(otherCampaigns));
        return this;
    }

    /**
     * Adds one or more coupons to the cart
     *
     * @param coupon       A coupon to add, cannot be null
     * @param otherCoupons Variable number of other coupons to add, cannot be null or contain null coupons
     *
     * @return Shopping cart itself after adding for chaining
     */
    public ShoppingCart addCoupons(Coupon coupon, Coupon... otherCoupons) {
        if (coupon == null || otherCoupons == null || Arrays.stream(otherCoupons).anyMatch(Objects::isNull)) throw new IllegalArgumentException("Shopping cart coupons cannot be null!");
        coupons.add(coupon);
        coupons.addAll(Arrays.asList(otherCoupons));
        return this;
    }

    /**
     * Processes all items, campaigns and coupons
     *
     * @return A {@link com.github.makiftutuncu.shoppingcart.Checkout} containing calculated values
     */
    public Checkout checkout() {
        return Checkout.calculate(items, groupedItems, campaigns, coupons);
    }

    /**
     * Calculates delivery cost for the items in the cart
     *
     * @return Delivery cost
     */
    public int deliveryCost() {
        return deliveryCostCalculator.calculateFor(groupedItems.keySet().size(), items.size());
    }

    /**
     * Prints out categories of items with their quantities and prices,
     * applied discounts by campaigns and coupons,
     * delivery cost and the final amount of the cart
     */
    public void print() {
        Checkout checkout = checkout();

        for (Map.Entry<Category, List<CartItem>> entry : groupedItems.entrySet()) {
            Category category              = entry.getKey();
            List<CartItem> itemsInCategory = entry.getValue();

            System.out.println("================================");
            System.out.println(category.title());
            System.out.println("================================");

            for (CartItem item : itemsInCategory) {
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

            Optional.ofNullable(checkout.campaignsByCategory().get(category)).ifPresent(campaign -> {
                System.out.printf("Applied Campaign: %s\n", campaign.toString());
                System.out.printf("Discount        : -%s\n\n", MoneyPrinter.print(checkout.campaignDiscountsByCategory().getOrDefault(category, 0)));
            });
        }

        int deliveryCost = deliveryCost();

        System.out.println("================================");
        System.out.println("Total Amount      :  " + MoneyPrinter.print(checkout.totalAmount()));
        System.out.println("Delivery Cost     :  " + MoneyPrinter.print(deliveryCost));

        if (checkout.totalCampaignDiscount() > 0) {
            System.out.println("Campaign Discounts: -" + MoneyPrinter.print(checkout.totalCampaignDiscount()));
        }

        checkout.appliedCoupon().ifPresent(coupon -> {
            System.out.println();
            System.out.printf("Applied Coupon: %s\n", coupon.toString());
            System.out.printf("Discount      : -%s\n", MoneyPrinter.print(checkout.totalCouponDiscount()));
        });

        System.out.println();
        System.out.println("Final Amount: " + MoneyPrinter.print(checkout.finalAmount() + deliveryCost));
        System.out.println("================================");
    }

    @Override public String toString() {
        return String.format("Shopping cart with %d item(s)", items.size());
    }
}
