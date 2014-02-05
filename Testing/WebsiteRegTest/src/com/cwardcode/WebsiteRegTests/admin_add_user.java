package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AddDeleteUser {
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
  public void testAddDeleteUser() throws Exception {
    driver.get(baseUrl + "/admin/auth/user/");
    driver.findElement(By.linkText("Add user")).click();
    driver.findElement(By.id("id_username")).clear();
    driver.findElement(By.id("id_username")).sendKeys("jqtest3");
    driver.findElement(By.id("id_password1")).clear();
    driver.findElement(By.id("id_password1")).sendKeys("Tester");
    driver.findElement(By.id("id_password2")).clear();
    driver.findElement(By.id("id_password2")).sendKeys("Tester");
    driver.findElement(By.name("_save")).click();
    driver.findElement(By.id("id_first_name")).clear();
    driver.findElement(By.id("id_first_name")).sendKeys("John");
    driver.findElement(By.id("id_last_name")).clear();
    driver.findElement(By.id("id_last_name")).sendKeys("Test");
    driver.findElement(By.id("id_email")).clear();
    driver.findElement(By.id("id_email")).sendKeys("jqtest@funtimeemail.glorb");
    driver.findElement(By.id("id_is_staff")).click();
    driver.findElement(By.id("id_is_superuser")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [addSelection | id=id_groups_from | label=SuperAdmin]]
    driver.findElement(By.id("id_groups_add_all_link")).click();
    driver.findElement(By.id("id_user_permissions_add_all_link")).click();
    driver.findElement(By.name("_save")).click();
    driver.findElement(By.xpath("(//input[@name='_selected_action'])[3]")).click();
    new Select(driver.findElement(By.name("action"))).selectByVisibleText("Delete selected users");
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
