package edu.depaul.se433.shoppingapp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.internal.matchers.Null;

public class partition {

    private ShoppingCart cart;
    private TotalCostCalculator calculator;

    @BeforeEach
    void setup() {

      // This loaded a default set of drivers.  See the PDF
      // for details
      calculator = new TotalCostCalculator();
      cart = new ShoppingCart();
    }

    @Test
    @DisplayName("Verify the system is running")
    void verifyBillIsProduced() {
      PurchaseItem item = new PurchaseItem("bagel",3.00,3);
      cart.addItem(item);

      Bill bill = calculator.calculate(cart, "IL", ShippingType.NEXT_DAY);
      assertEquals(Bill.class, bill.getClass());
    }

    private static Stream<Arguments> SNpartitionInputs() {

      return Stream.of(
          Arguments.of(new PurchaseItem("bagel",25.00,1),"IL",ShippingType.STANDARD,36.5),
          Arguments.of(new PurchaseItem("bagel",25.00,1),"IL",ShippingType.NEXT_DAY,51.5),
          Arguments.of(new PurchaseItem("bagel",25.00,1),"OH",ShippingType.STANDARD,35),
          Arguments.of(new PurchaseItem("bagel",25.00,1),"OH",ShippingType.NEXT_DAY,50),
          Arguments.of(new PurchaseItem("bagel",50000,1),"IL",ShippingType.STANDARD,53000),
          Arguments.of(new PurchaseItem("bagel",50000,1),"IL",ShippingType.NEXT_DAY,53025),
          Arguments.of(new PurchaseItem("bagel",50000,1),"OH",ShippingType.STANDARD,50000),
          Arguments.of(new PurchaseItem("bagel",50000,1),"OH",ShippingType.NEXT_DAY,50025)
      );
    }

    @ParameterizedTest
    @MethodSource("SNpartitionInputs")
    @DisplayName("Strong/Normal Partition Tests")
    void pTest(PurchaseItem item, String state, ShippingType shipping, double expected) {
      cart.addItem(item);

      Bill bill = calculator.calculate(cart, state,shipping);
      assertEquals(expected, bill.getTotal());
    }

  private static Stream<Arguments> WRpartitionInputs() {

    return Stream.of(
        Arguments.of(new PurchaseItem("bagel",25.00,1),"IL",ShippingType.STANDARD,36.5),
        Arguments.of(new PurchaseItem("bagel",50000,1),"OH",ShippingType.NEXT_DAY,50000)
    );
  }

  private static Stream<Arguments> WRpartitionInputs2() {

    return Stream.of(
        Arguments.of(new PurchaseItem("bagel",.5,1),"OH",ShippingType.STANDARD),
        Arguments.of(new PurchaseItem("bagel",190000,1),"IL",ShippingType.NEXT_DAY),
        Arguments.of(new PurchaseItem("bagel",25,1),"CAT",ShippingType.NEXT_DAY),
        Arguments.of(new PurchaseItem("bagel",25,1),"",ShippingType.STANDARD),
        Arguments.of(new PurchaseItem("bagel",25,1),"OH",null)
    );
  }

  @ParameterizedTest
  @MethodSource("WRpartitionInputs")
  @DisplayName("Weak / Robust Partition Test 1")
  void pTest2(PurchaseItem item, String state, ShippingType shipping, double expected) {
    cart.addItem(item);

    Bill bill = calculator.calculate(cart, state,shipping);
    assertEquals(expected, bill.getTotal());
  }

  @ParameterizedTest
  @MethodSource("WRpartitionInputs2")
  @DisplayName("Weak / Robust Partition Test 2")
  void pTest3(PurchaseItem item, String state, ShippingType shipping) {
    cart.addItem(item);
    assertThrows(Exception.class, () -> {calculator.calculate(cart, state,shipping);});
  }



  }


