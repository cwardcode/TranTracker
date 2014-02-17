package com.cwardcode.WebsiteRegTests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class UserSearch extends TestCase {
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
  public void testUserSearch() throws Exception {
    driver.get(baseUrl + "/admin/auth/user/");
    driver.findElement(By.id("searchbar")).clear();
    driver.findElement(By.id("searchbar")).sendKeys("name");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.cssSelector("select.auto-width.search-filter"))).selectByVisibleText("Staff status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[2]"))).selectByVisibleText("Superuser status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[3]"))).selectByVisibleText("Active");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[2]"))).selectByVisibleText("Superuser status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[2]"))).selectByVisibleText("Superuser status");
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[3]"))).selectByVisibleText("---");
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[3]"))).selectByVisibleText("Active");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[3]"))).selectByVisibleText("---");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.cssSelector("select.auto-width.search-filter"))).selectByVisibleText("Staff status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.id("searchbar")).clear();
    driver.findElement(By.id("searchbar")).sendKeys("hlthomas2");
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[2]"))).selectByVisibleText("Superuser status");
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[3]"))).selectByVisibleText("---");
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[3]"))).selectByVisibleText("Active");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[2]"))).selectByVisibleText("Superuser status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.cssSelector("select.auto-width.search-filter"))).selectByVisibleText("Staff status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[2]"))).selectByVisibleText("Superuser status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[3]"))).selectByVisibleText("Active");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[2]"))).selectByVisibleText("Superuser status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    new Select(driver.findElement(By.cssSelector("select.auto-width.search-filter"))).selectByVisibleText("Staff status");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
    driver.findElement(By.id("searchbar")).clear();
    driver.findElement(By.id("searchbar")).sendKeys("");
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[2]"))).selectByVisibleText("Superuser status");
    new Select(driver.findElement(By.xpath("//form[@id='changelist-search']/div/span/select[3]"))).selectByVisibleText("Active");
    driver.findElement(By.cssSelector("input.btn.btn-info")).click();
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
