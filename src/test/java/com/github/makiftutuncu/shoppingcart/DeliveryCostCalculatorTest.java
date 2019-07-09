package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryCostCalculatorTest {
    @Test void failToCreateDeliveryCostCalculatorWithInvalidCostPerCategory() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryCostCalculator(-1, 100, 100));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryCostCalculator(0, 100, 100));
    }

    @Test void failToCreateDeliveryCostCalculatorWithInvalidCostPerProduct() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryCostCalculator(100, -1, 100));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryCostCalculator(100, 0, 100));
    }

    @Test void failToCreateDeliveryCostCalculatorWithInvalidFixedCost() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryCostCalculator(100, 100, -1));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryCostCalculator(100, 100, 0));
    }

    @Test void failToCalculateForNumberOfCategory() {
        DeliveryCostCalculator calculator = new DeliveryCostCalculator(100, 100, 100);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFor(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFor(0, 1));
    }

    @Test void failToCalculateForNumberOfProduct() {
        DeliveryCostCalculator calculator = new DeliveryCostCalculator(100, 100, 100);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFor(1, -1));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFor(1, 0));
    }

    @Test void calculateDeliveryCost() {
        DeliveryCostCalculator calculator = new DeliveryCostCalculator(200, 100, 300);
        assertEquals(calculator.calculateFor(2, 3), 1000);
    }
}
