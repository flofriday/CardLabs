import pytest
import helper
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait

def test_redirect_to_dashboard_page_after_login(driver_headless):
    driver = driver_headless
    helper.login(driver, "test1", "pass1")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "profile_pic_in_navbar")))
    assert driver.find_element(By.ID, "leftHeading").text == "Dashboard"

def test_redirect_to_dashboard_when_clicking_on_card_labs(driver_headless):
    driver = driver_headless
    helper.login(driver, "test1", "pass1")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "profile_pic_in_navbar")))
    driver.find_element(By.ID, "home_authenticated")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "leftHeading")))
    assert driver.find_element(By.ID, "leftHeading").text == "Dashboard"

def test_unauthenticated_page_is_shown_when_accessing_dashboard_unauthenticated(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/dashboard")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "unautherizedPage")))
    assert driver.find_element(By.ID, "unautherizedPage").text == "Not authorized :c"
