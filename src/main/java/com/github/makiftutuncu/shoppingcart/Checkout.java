package com.github.makiftutuncu.shoppingcart;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the result of a calculation of a shopping cart
 */
public class Checkout {
    private int totalAmount;
    private int totalCampaignDiscount;
    private int totalCouponDiscount;
    private Map<Category, Campaign> campaignsByCategory;
    private Map<Category, Integer> campaignDiscountsByCategory;
    private Optional<Coupon> appliedCoupon;

    private Set<Campaign> usedCampaigns;

    private Checkout() {
        this.totalAmount                 = 0;
        this.totalCampaignDiscount       = 0;
        this.totalCouponDiscount         = 0;
        this.campaignsByCategory         = new HashMap<>();
        this.campaignDiscountsByCategory = new HashMap<>();
        this.appliedCoupon               = Optional.empty();
        this.usedCampaigns               = new HashSet<>();
    }

    /**
     * @return Total amount of items in the cart
     */
    public int totalAmount() {
        return totalAmount;
    }

    /**
     * @return Total campaign discount applied
     */
    public int totalCampaignDiscount() {
        return totalCampaignDiscount;
    }

    /**
     * @return Total coupon discount applied
     */
    public int totalCouponDiscount() {
        return totalCouponDiscount;
    }

    /**
     * @return Final amount to pay after applying total campaign discount and total coupon discount
     */
    public int finalAmount() {
        return Math.max(0, totalAmount - totalCampaignDiscount - totalCouponDiscount);
    }

    /**
     * @return A map of applied campaigns by categories
     */
    public Map<Category, Campaign> campaignsByCategory() {
        return campaignsByCategory;
    }

    /**
     * @return A map of the applied campaign discounts by categories
     */
    public Map<Category, Integer> campaignDiscountsByCategory() {
        return campaignDiscountsByCategory;
    }

    /**
     * @return Applied coupon optionally, if any, `Optional.empty()` otherwise
     */
    public Optional<Coupon> appliedCoupon() {
        return appliedCoupon;
    }

    /**
     * Processes all items, campaigns and coupons
     *
     * @param items        Items in the cart
     * @param groupedItems Items in the cart grouped by their categories
     * @param campaigns    Campaigns added to the cart
     * @param coupons      Coupons added to the cart
     *
     * @return A {@link com.github.makiftutuncu.shoppingcart.Checkout} containing the results
     */
    public static Checkout calculate(Map<UUID, CartItem> items,
                                     Map<Category, List<CartItem>> groupedItems,
                                     List<Campaign> campaigns,
                                     List<Coupon> coupons) {
        Checkout checkout = new Checkout();

        for (Map.Entry<Category, List<CartItem>> entry : groupedItems.entrySet()) {
            Category category = entry.getKey();

            int totalAmountForCategory =
                    entry.getValue()
                            .stream()
                            .map(CartItem::totalAmount)
                            .reduce(0, Integer::sum);

            checkout.totalAmount += totalAmountForCategory;

            Optional<Campaign> maybeCampaign = findBestCampaign(new ArrayList<>(items.values()), campaigns, checkout.usedCampaigns, entry.getKey());

            int campaignDiscount = maybeCampaign.map(campaign -> campaign.discountFor(totalAmountForCategory)).orElse(0);

            maybeCampaign.ifPresent(campaign -> {
                checkout.campaignsByCategory.put(category, campaign);
                checkout.campaignDiscountsByCategory.put(category, campaignDiscount);
                checkout.usedCampaigns.add(campaign);
            });

            checkout.totalCampaignDiscount += campaignDiscount;
        }

        int cartAmount = checkout.totalAmount - checkout.totalCampaignDiscount;

        checkout.appliedCoupon = findBestCoupon(coupons, cartAmount);

        checkout.totalCouponDiscount = checkout.appliedCoupon.map(coupon -> coupon.discountFor(cartAmount)).orElse(0);

        return checkout;
    }

    private static Optional<Campaign> findBestCampaign(List<CartItem> items,
                                                       List<Campaign> campaigns,
                                                       Set<Campaign> usedCampaigns,
                                                       Category category) {
        List<CartItem> itemsInCategory =
                items.stream()
                     .filter(item -> item.product().category().isChild(category) || item.product().category().equals(category))
                     .collect(Collectors.toList());

        int numberOfItemsInCategory = itemsInCategory.size();
        int totalAmountForCategory  = itemsInCategory.stream().map(CartItem::totalAmount).reduce(0, Integer::sum);

        return campaigns.stream()
                .filter(campaign -> {
                    boolean notUsedBefore      = !usedCampaigns.contains(campaign);
                    boolean hasCorrectCategory = category.isChild(campaign.category()) || campaign.category().equals(category);
                    boolean hasEnoughItems     = numberOfItemsInCategory >= campaign.numberOfItems();

                    return notUsedBefore && hasCorrectCategory && hasEnoughItems;
                })
                .max(Comparator.comparingInt(c -> c.discountFor(totalAmountForCategory)));
    }

    private static Optional<Coupon> findBestCoupon(List<Coupon> coupons, int cartAmount) {
        return coupons.stream()
                .filter(c -> c.minimumAmount() <= cartAmount)
                .max(Comparator.comparingInt(c -> c.discountFor(cartAmount)));
    }
}
