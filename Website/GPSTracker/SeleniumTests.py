__author__ = 'chris'
from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.common.exceptions import NoSuchElementException, NoAlertPresentException
import unittest


class Unittest(unittest.TestCase):
    def setUp(self):
        self.driver = webdriver.Firefox()
        self.driver.implicitly_wait(30)
        self.base_url = "http://localhost:8000"
        self.verificationErrors = []
        self.accept_next_alert = True

    def test_unit(self):
        driver = self.driver
        driver.get(self.base_url + "/")
        driver.find_element_by_link_text("All-Campus").click()
        driver.find_element_by_link_text("Village Express").click()
        driver.find_element_by_link_text("HHS Express").click()
        driver.find_element_by_link_text("Off-Campus North").click()
        driver.find_element_by_link_text("Off-Campus South").click()
        driver.find_element_by_css_selector("option[value=\"1\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("S. Baseball")
        driver.find_element_by_css_selector("option[value=\"2\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Ramsey")
        driver.find_element_by_css_selector("option[value=\"3\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Field House")
        driver.find_element_by_css_selector("option[value=\"4\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Bardo Arts Center")
        driver.find_element_by_css_selector("option[value=\"5\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Dining Hall")
        driver.find_element_by_css_selector("option[value=\"6\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Walker")
        driver.find_element_by_css_selector("option[value=\"7\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Village")
        driver.find_element_by_css_selector("option[value=\"8\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Norton")
        driver.find_element_by_css_selector("option[value=\"9\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("UC")
        driver.find_element_by_css_selector("option[value=\"10\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Library")
        driver.find_element_by_css_selector("option[value=\"11\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Moore")
        driver.find_element_by_css_selector("option[value=\"12\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Reynolds")
        driver.find_element_by_css_selector("option[value=\"13\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("McKee")
        driver.find_element_by_css_selector("option[value=\"14\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Albright/Benton")
        driver.find_element_by_css_selector("option[value=\"15\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Harrill")
        driver.find_element_by_css_selector("option[value=\"16\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Central")
        driver.find_element_by_css_selector("option[value=\"17\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("OneStop")
        driver.find_element_by_css_selector("option[value=\"18\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Coulter")
        driver.find_element_by_css_selector("option[value=\"19\"]").click()
        Select(driver.find_element_by_id("content-lb")).select_by_visible_text("Kimmell")
        driver.find_element_by_css_selector("option[value=\"20\"]").click()
        driver.find_element_by_css_selector("button[type=\"button\"]").click()
        driver.find_element_by_link_text("Shuttle Speak").click()
        driver.find_element_by_link_text("Help").click()
        driver.find_element_by_link_text("Contact Us").click()
        driver.find_element_by_link_text("About Us").click()
        driver.find_element_by_id("header-logo").click()

    def is_element_present(self, how, what):
        try: self.driver.find_element(by=how, value=what)
        except NoSuchElementException, e: return False
        return True

    def is_alert_present(self):
        try: self.driver.switch_to_alert()
        except NoAlertPresentException, e: return False
        return True

    def close_alert_and_get_its_text(self):
        try:
            alert = self.driver.switch_to_alert()
            alert_text = alert.text
            if self.accept_next_alert:
                alert.accept()
            else:
                alert.dismiss()
            return alert_text
        finally: self.accept_next_alert = True

    def tearDown(self):
        self.driver.quit()
        self.assertEqual([], self.verificationErrors)

if __name__ == "__main__":
    unittest.main()
