package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CampaignTest {
    private Category food = new Category("Food");

    @Test void failToCreateCampaignWithInvalidCategory() {
        assertThrows(IllegalArgumentException.class, () -> Campaign.ofAmount(null, 1, 100));
    }

    @Test void failToCreateCampaignWithInvalidNumberOfItems() {
        assertThrows(IllegalArgumentException.class, () -> Campaign.ofAmount(food, 0, 100));
        assertThrows(IllegalArgumentException.class, () -> Campaign.ofAmount(food, -1, 100));
    }

    @Test void failToCreateCampaignWithInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> Campaign.ofAmount(food, 1, -100));
        assertThrows(IllegalArgumentException.class, () -> Campaign.ofAmount(food, 1, 0));
    }

    @Test void failToCreateCampaignWithInvalidRate() {
        assertThrows(IllegalArgumentException.class, () -> Campaign.ofRate(food, 1, -0.5));
        assertThrows(IllegalArgumentException.class, () -> Campaign.ofRate(food, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> Campaign.ofRate(food, 1, 1.5));
    }

    @Test void createCampaignWithAmount() {
        Campaign campaign = Campaign.ofAmount(food, 1, 100);
        assertEquals(campaign.category(), food);
        assertEquals(campaign.numberOfItems(), 1);
        assertEquals(campaign.toString(), "₺1.00 off for 1 or more Food");
    }

    @Test void calculateDiscountBasedOnAmount() {
        Campaign campaign = Campaign.ofAmount(food, 1, 100);
        assertEquals(campaign.discountFor(300), 100);
        assertEquals(campaign.discountFor(500), 100);
    }

    @Test void createCampaignWithRate() {
        Campaign campaign = Campaign.ofRate(food, 2, 0.20);
        assertEquals(campaign.category(), food);
        assertEquals(campaign.numberOfItems(), 2);
        assertEquals(campaign.toString(), "20% off for 2 or more Food");
    }

    @Test void calculateDiscountBasedOnRate() {
        Campaign campaign = Campaign.ofRate(food, 2, 0.20);
        assertEquals(campaign.discountFor(300), 60);
        assertEquals(campaign.discountFor(500), 100);
    }
}
