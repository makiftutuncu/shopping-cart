package com.github.makiftutuncu.shoppingcart;

import java.util.*;
import java.util.stream.Collectors;

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
    private Set<Campaign> usedCampaigns                = new HashSet<>();

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
     * Calculates cart total, total campaign discounts and total coupon discounts by processing all items in the cart
     *
     * @return An int array of size 3 containing cart total, total campaign discounts and total coupon discounts respectively
     */
    public int[] calculateAmounts() {
        int totalAmount = 0;
        int totalCampaignDiscount = 0;

        usedCampaigns.clear();

        for (Map.Entry<Category, List<CartItem>> entry : groupedItems.entrySet()) {
            int totalAmountForCategory =
                    entry.getValue()
                         .stream()
                         .map(CartItem::totalAmount)
                         .reduce(0, Integer::sum);

            totalAmount += totalAmountForCategory;

            int campaignDiscount = findBestCampaignFor(entry.getKey())
                    .map(campaign -> {
                        usedCampaigns.add(campaign);
                        return campaign.discountFor(totalAmountForCategory);
                    })
                    .orElse(0);

            totalCampaignDiscount += campaignDiscount;
        }

        int cartAmount = totalAmount - totalCampaignDiscount;

        int couponDiscount = findBestCoupon(cartAmount).map(coupon -> coupon.discountFor(cartAmount)).orElse(0);

        return new int[] { totalAmount, totalCampaignDiscount, couponDiscount };
    }

    /**
     * Calculates total campaign discounts by processing all items in the cart
     *
     * @return Total campaign discounts
     */
    public int campaignDiscount() {
        int[] amounts = calculateAmounts();
        return amounts[1];
    }

    /**
     * Calculates total coupon discounts by processing all items in the cart
     *
     * @return Total coupon discounts
     */
    public int couponDiscount() {
        int[] amounts = calculateAmounts();
        return amounts[2];
    }

    /**
     * Calculates cart total after applying total campaign discounts and total coupon discounts by processing all items in the cart
     *
     * @return Cart total after applying total campaign discounts and total coupon discounts
     */
    public int totalAmountAfterDiscounts() {
        int[] amounts = calculateAmounts();

        return Math.max(amounts[0] - amounts[1] - amounts[2], 0);
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
     * Finds campaign that makes the most discount for given category
     *
     * @param category Category for which best campaign is sought
     *
     * @return Best campaign optionally if found or `Optional.empty()`
     */
    public Optional<Campaign> findBestCampaignFor(Category category) {
        List<CartItem> itemsInCategory =
                items.values()
                        .stream()
                        .filter(item -> item.product().category().isChild(category) || item.product().category().equals(category))
                        .collect(Collectors.toList());

        int numberOfItemsInCategory = itemsInCategory.size();
        int totalAmountForCategory  = itemsInCategory.stream().map(CartItem::totalAmount).reduce(0, Integer::sum);

        return campaigns.stream()
                .filter(campaign -> !usedCampaigns.contains(campaign) && (category.isChild(campaign.category()) || campaign.category().equals(category)) && numberOfItemsInCategory >= campaign.numberOfItems())
                .max(Comparator.comparingInt(c -> c.discountFor(totalAmountForCategory)));
    }

    /**
     * Finds coupon that makes the most discount for given cart amount
     *
     * @param cartAmount Cart amount for which best coupon is sought
     *
     * @return Best coupon optionally if found or `Optional.empty()`
     */
    public Optional<Coupon> findBestCoupon(int cartAmount) {
        return coupons.stream()
                .filter(c -> c.minimumAmount() <= cartAmount)
                .max(Comparator.comparingInt(c -> c.discountFor(cartAmount)));
    }

    /**
     * Prints out categories of items with their quantities and prices,
     * applied discounts by campaigns and coupons,
     * delivery cost and the final amount of the cart
     */
    public void print() {
        int totalAmount = 0;
        int totalCampaignDiscount = 0;

        usedCampaigns.clear();

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
                        usedCampaigns.add(campaign);
                        System.out.printf("Applied Campaign: %s\n", campaign.toString());
                        System.out.printf("Discount        : -%s\n\n", MoneyPrinter.print(discount));
                        return discount;
                    })
                    .orElse(0);

            totalCampaignDiscount += campaignDiscount;
        }

        int cartAmount   = Math.max(totalAmount - totalCampaignDiscount, 0);
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

        int finalAmount = Math.max(cartAmount - couponDiscount, 0) + deliveryCost;

        System.out.println();
        System.out.println("Final Amount: " + MoneyPrinter.print(finalAmount));
        System.out.println("================================");
    }

    @Override public String toString() {
        return String.format("Shopping cart with %d item(s)", items.size());
    }
}
