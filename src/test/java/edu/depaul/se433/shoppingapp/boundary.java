package edu.depaul.se433.shoppingapp;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class boundary {

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

    Bill bill = calculator.calculate(cart, "IL",ShippingType.NEXT_DAY);
    assertEquals(Bill.class, bill.getClass());
  }

  private static Stream<Arguments> boundaryInputs() {

    return Stream.of(
        Arguments.of(new PurchaseItem("bagel",1.00,1),"IL",ShippingType.STANDARD,11.06),
        Arguments.of(new PurchaseItem("bagel",10.00,1),"IL",ShippingType.STANDARD,20.60),
        Arguments.of(new PurchaseItem("bagel",50000.00,1),"IL",ShippingType.STANDARD,53000),
        Arguments.of(new PurchaseItem("bagel",95000.00,1),"IL",ShippingType.STANDARD,100700),
        Arguments.of(new PurchaseItem("bagel",99999.99,1),"IL",ShippingType.STANDARD,105999.99),
        Arguments.of(new PurchaseItem("bagel",1.00,1),"OH",ShippingType.STANDARD,11),
        Arguments.of(new PurchaseItem("bagel",10.00,1),"OH",ShippingType.STANDARD,20),
        Arguments.of(new PurchaseItem("bagel",50000.00,1),"OH",ShippingType.STANDARD,50000),
        Arguments.of(new PurchaseItem("bagel",95000.00,1),"OH",ShippingType.STANDARD,95000),
        Arguments.of(new PurchaseItem("bagel",99999.99,1),"OH",ShippingType.STANDARD,99999.99),
        Arguments.of(new PurchaseItem("bagel",1.00,1),"IL",ShippingType.NEXT_DAY,26.06),
        Arguments.of(new PurchaseItem("bagel",10.00,1),"IL",ShippingType.NEXT_DAY,35.60),
        Arguments.of(new PurchaseItem("bagel",50000.00,1),"IL",ShippingType.NEXT_DAY,53025),
        Arguments.of(new PurchaseItem("bagel",95000.00,1),"IL",ShippingType.NEXT_DAY,100725),
        Arguments.of(new PurchaseItem("bagel",99999.99,1),"IL",ShippingType.NEXT_DAY,106024.99),
        Arguments.of(new PurchaseItem("bagel",1.00,1),"OH",ShippingType.NEXT_DAY,26.00),
        Arguments.of(new PurchaseItem("bagel",10.00,1),"OH",ShippingType.NEXT_DAY,35.00),
        Arguments.of(new PurchaseItem("bagel",50000.00,1),"OH",ShippingType.NEXT_DAY,50025),
        Arguments.of(new PurchaseItem("bagel",95000.00,1),"OH",ShippingType.NEXT_DAY,95025),
        Arguments.of(new PurchaseItem("bagel",99999.99,1),"OH",ShippingType.NEXT_DAY,100024.99)
    );
  }

  @ParameterizedTest
  @MethodSource("boundaryInputs")
  @DisplayName("Boundary Tests")
  void bTest(PurchaseItem item, String state, ShippingType shipping, double expected) {
    cart.addItem(item);

    Bill bill = calculator.calculate(cart, state,shipping);
    assertEquals(expected, bill.getTotal());
  }





}