import pytest
import helper
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from random import randint
import time

def test_delete_existing_user(driver_headless):
    driver = driver_headless
    username = "deleteTest" + str(randint(0,99999999999))
    helper.register(driver, username, "delete@test.com" + str(randint(0,99999999999)), "deletePass1!",  "Austria")

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".text-6xl")))
    assert driver.find_element(By.CSS_SELECTOR, ".text-6xl").text == "Settings"
    assert driver.find_element(By.CSS_SELECTOR, ".bg-accent").text == "Delete Account"
    driver.find_element(By.CSS_SELECTOR, ".bg-accent").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".flex:nth-child(1) > .bg-accent")))
    driver.find_element(By.CSS_SELECTOR, ".flex:nth-child(1) > .bg-accent").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "login_button_navbar")))
    driver.find_element(By.ID, "login_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys(username)
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys("deletePass1!")
    driver.find_element(By.ID, "login_button").click()

    assert driver.find_element(By.CSS_SELECTOR, ".absolute").text == "Login"

def test_change_website_notifications_updates_setting(driver):
    helper.login(driver, "test1", "pass1")

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "profile_pic_in_navbar")))
    driver.find_element(By.ID, "profile_pic_in_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "settings_button_navbar")))
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "sendWebsiteUpdate")))
    driver.find_element(By.ID, "sendWebsiteUpdate").click()
    driver.find_element(By.CSS_SELECTOR, ".btn:nth-child(2)").click()

    driver.find_element(By.LINK_TEXT, "Card Labs").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "profile_pic_in_navbar")))
    driver.find_element(By.ID, "profile_pic_in_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "settings_button_navbar")))
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "sendWebsiteUpdate")))
    assert driver.find_element(By.ID, "sendWebsiteUpdate").is_selected() is True
    driver.find_element(By.ID, "sendWebsiteUpdate").click()
    driver.find_element(By.CSS_SELECTOR, ".bg-secondary").click()
    driver.find_element(By.CSS_SELECTOR, ".btn:nth-child(2)").click()


def test_change_website_notifications_updates_setting(driver):
    helper.login(driver, "test1", "pass1")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "sendScoreUpdate")))
    driver.find_element(By.ID, "sendScoreUpdate").click()
    driver.find_element(By.CSS_SELECTOR, ".btn:nth-child(2)").click()

    driver.find_element(By.LINK_TEXT, "Card Labs").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "sendScoreUpdate")))
    assert driver.find_element(By.ID, "sendScoreUpdate").is_selected() is True
    driver.find_element(By.ID, "sendScoreUpdate").click()
    driver.find_element(By.CSS_SELECTOR, ".bg-secondary").click()
    driver.find_element(By.CSS_SELECTOR, ".btn:nth-child(2)").click()

def test_change_newletter_setting(driver):
    helper.login(driver, "test1", "pass1")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "sendNewsletter")))
    driver.find_element(By.ID, "sendNewsletter").click()
    driver.find_element(By.CSS_SELECTOR, ".btn:nth-child(2)").click()

    driver.find_element(By.LINK_TEXT, "Card Labs").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "sendNewsletter")))
    assert driver.find_element(By.ID, "sendNewsletter").is_selected() is True
    driver.find_element(By.ID, "sendNewsletter").click()
    driver.find_element(By.CSS_SELECTOR, ".bg-secondary").click()
    driver.find_element(By.CSS_SELECTOR, ".btn:nth-child(2)").click()

def test_change_location(driver):
    helper.login(driver, "test1", "pass1")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, "#dropdown-button > .flex")))
    driver.find_element(By.CSS_SELECTOR, "#dropdown-button > .flex").click()
    driver.find_element(By.LINK_TEXT, "Denmark").click()
    driver.find_element(By.CSS_SELECTOR, ".h-full > .items-center").click()
    assert driver.find_element(By.CSS_SELECTOR, "#dropdown-button > .flex").text == "Denmark"
    driver.find_element(By.CSS_SELECTOR, ".btn:nth-child(2)").click()
    driver.find_element(By.LINK_TEXT, "Card Labs").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()
    assert driver.find_element(By.CSS_SELECTOR, "#dropdown-button > .flex").text == "Denmark"
    driver.find_element(By.CSS_SELECTOR, "#dropdown-button > .flex").click()
    driver.find_element(By.LINK_TEXT, "Austria").click()
    driver.find_element(By.CSS_SELECTOR, ".btn:nth-child(2)").click()
