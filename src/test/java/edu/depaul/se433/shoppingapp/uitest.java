package edu.depaul.se433.shoppingapp;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

@Tag("End2end")
public class uitest {

  private static WebDriver driver;
  private SoftAssertions softly;

  @BeforeAll
  static void setup() {
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  void setupSoftly() {

    softly = new SoftAssertions();
    driver = new ChromeDriver();
  }

  @AfterEach
  void executeVerification() {

    softly.assertAll();
    driver.quit();
  }

  @Test
  @DisplayName("add an item to cart")
  void testAddCart() {
    //opens page and makes sure title is the same as expected
    driver.get("http://localhost:8085/?#");
    String title = driver.getTitle();
    softly.assertThat(title).as("title").isEqualTo("Online Shopping");

    WebElement nameField = driver.findElement(By.id("customer-name"));
    nameField.sendKeys("Dave");

    WebElement stateField = driver.findElement(By.id("state"));
    stateField.sendKeys("IN");

    WebElement shipping = driver.findElement(By.id("shipping"));
    shipping.sendKeys("STANDARD");

    WebElement productFiled = driver.findElement(By.id("name"));
    productFiled.sendKeys("Apple");

    WebElement unitFiled = driver.findElement(By.id("unit_price"));
    unitFiled.sendKeys("2.00");

    WebElement quantityFiled = driver.findElement(By.id("quantity"));
    quantityFiled.sendKeys("5");

    WebElement addItemButton = driver.findElement(By.id("add-item-btn"));
    addItemButton.click();

    WebDriverWait wait = new WebDriverWait(driver, 1);
//
    WebElement resultDiv = driver.findElement(By.id("result"));
    wait.until(ExpectedConditions.textToBePresentInElement(resultDiv, "Cart"));
    String msg = resultDiv.getText();
    softly.assertThat(msg).as("name contents").isEqualTo("Cart contains 1 items");
  }
  @Test
  @DisplayName("make sure content total is correct and  average")
    void testTotal() {

      driver.get("http://localhost:8085/?#");
      String title = driver.getTitle();
      softly.assertThat(title).as("title").isEqualTo("Online Shopping");

      WebElement nameField = driver.findElement(By.id("customer-name"));
        nameField.sendKeys("Tom");

      WebElement stateField = driver.findElement(By.id("state"));
      stateField.sendKeys("IN");

      WebElement shipping = driver.findElement(By.id("shipping"));
      shipping.sendKeys("STANDARD");

      WebElement productFiled = driver.findElement(By.id("name"));
      productFiled.sendKeys("Apple");

      WebElement unitFiled = driver.findElement(By.id("unit_price"));
      unitFiled.sendKeys("2.00");

      WebElement quantityFiled = driver.findElement(By.id("quantity"));
      quantityFiled.sendKeys("5");

      WebElement addItemButton = driver.findElement(By.id("add-item-btn"));
      addItemButton.click();

    WebElement totalButton = driver.findElement(By.id("get-price-btn"));
    totalButton.click();

      WebDriverWait wait = new WebDriverWait(driver, 1);
  //
      WebElement resultDiv = driver.findElement(By.id("result"));
      wait.until(ExpectedConditions.textToBePresentInElement(resultDiv, "total"));
      String msg = resultDiv.getText();
      softly.assertThat(msg).as("name contents").isEqualTo("total: 20");

    WebElement checkoutButton = driver.findElement(By.id("checkout-btn"));
    checkoutButton.click();

    WebElement avDiv = driver.findElement(By.id("avg"));
    wait.until(ExpectedConditions.visibilityOf(avDiv));
     msg = avDiv.getText();
    softly.assertThat(msg).as("name contents").isEqualTo("20.00");

  }

  }

