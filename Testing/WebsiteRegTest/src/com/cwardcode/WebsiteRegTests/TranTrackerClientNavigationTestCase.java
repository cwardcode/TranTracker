package com.cwardcode.WebsiteRegTests;

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

public class TranTrackerClientNavigationTestCase extends TestCase {
    private Selenium selenium;

    @Before
    public void setUp() throws Exception {
        selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://tracker.cwardcode.com/");
        selenium.start();
    }


    public void testTranTrackerClientNavigationTestCase() throws Exception {
        selenium.open("/");
        selenium.click("link=All-Campus");
        selenium.click("link=Village Express");
        selenium.click("link=HHS Express");
        selenium.click("link=Off-Campus North");
        selenium.click("link=Off-Campus South");
        selenium.click("css=option[value=\"1\"]");
        selenium.select("id=content-lb", "label=S. Baseball");
        selenium.click("css=option[value=\"2\"]");
        selenium.select("id=content-lb", "label=Ramsey");
        selenium.click("css=option[value=\"3\"]");
        selenium.select("id=content-lb", "label=Field House");
        selenium.click("css=option[value=\"4\"]");
        selenium.select("id=content-lb", "label=Bardo Arts Center");
        selenium.click("css=option[value=\"5\"]");
        selenium.select("id=content-lb", "label=Dining Hall");
        selenium.click("css=option[value=\"6\"]");
        selenium.select("id=content-lb", "label=Walker");
        selenium.click("css=option[value=\"7\"]");
        selenium.select("id=content-lb", "label=Village");
        selenium.click("css=option[value=\"8\"]");
        selenium.select("id=content-lb", "label=Norton");
        selenium.click("css=option[value=\"9\"]");
        selenium.select("id=content-lb", "label=UC");
        selenium.click("css=option[value=\"10\"]");
        selenium.select("id=content-lb", "label=Library");
        selenium.click("css=option[value=\"11\"]");
        selenium.select("id=content-lb", "label=Moore");
        selenium.click("css=option[value=\"12\"]");
        selenium.select("id=content-lb", "label=Reynolds");
        selenium.click("css=option[value=\"13\"]");
        selenium.select("id=content-lb", "label=McKee");
        selenium.click("css=option[value=\"14\"]");
        selenium.select("id=content-lb", "label=Albright/Benton");
        selenium.click("css=option[value=\"15\"]");
        selenium.select("id=content-lb", "label=Harrill");
        selenium.click("css=option[value=\"16\"]");
        selenium.select("id=content-lb", "label=Central");
        selenium.click("css=option[value=\"17\"]");
        selenium.select("id=content-lb", "label=OneStop");
        selenium.click("css=option[value=\"18\"]");
        selenium.select("id=content-lb", "label=Coulter");
        selenium.click("css=option[value=\"19\"]");
        selenium.select("id=content-lb", "label=Kimmell");
        selenium.click("css=option[value=\"20\"]");
        selenium.click("css=button[type=\"button\"]");
        selenium.click("link=Shuttle Speak");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Help");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Contact Us");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=About Us");
        selenium.waitForPageToLoad("30000");
        selenium.click("id=header-logo");
        selenium.waitForPageToLoad("30000");
    }

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}
