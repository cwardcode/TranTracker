package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AddDeleteStop {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://tracker.cwardcode.com/admin/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testAddDeleteStop() throws Exception {
    driver.get(baseUrl + "/admin/tracker/stoplocation/");
    driver.findElement(By.linkText("Add stop location")).click();
    driver.findElement(By.id("id_StopName")).clear();
    driver.findElement(By.id("id_StopName")).sendKeys("Test");
    driver.findElement(By.id("id_Latitude")).clear();
    driver.findElement(By.id("id_Latitude")).sendKeys("0");
    driver.findElement(By.id("id_Longitude")).clear();
    driver.findElement(By.id("id_Longitude")).sendKeys("0");
    driver.findElement(By.name("_save")).click();
    driver.findElement(By.name("_selected_action")).click();
    new Select(driver.findElement(By.name("action"))).selectByVisibleText("Delete selected stop locations");
    driver.findElement(By.name("index")).click();
    driver.findElement(By.cssSelector("input.btn.btn-danger")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
