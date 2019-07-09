package com.github.makiftutuncu.shoppingcart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyPrinterTest {
    @Test void printWithDefaults() {
        assertEquals(MoneyPrinter.print(-1), "-₺0.01");
        assertEquals(MoneyPrinter.print(-123), "-₺1.23");
        assertEquals(MoneyPrinter.print(0), "₺0.00");
        assertEquals(MoneyPrinter.print(1), "₺0.01");
        assertEquals(MoneyPrinter.print(123), "₺1.23");
    }

    @Test void printWithCustomSettings() {
        assertEquals(MoneyPrinter.print(123, "$", true), "$1.23");
        assertEquals(MoneyPrinter.print(123, " liras", false), "1.23 liras");
    }
}
