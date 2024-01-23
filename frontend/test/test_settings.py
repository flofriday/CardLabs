import pytest
import helper
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from random import randint
import time

def test_delete_existing_user(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999901, "test5@email")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "settings_button_navbar")))
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".text-6xl")))
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".bg-accent")))
    assert driver.find_element(By.CSS_SELECTOR, ".text-6xl").text == "Settings"
    assert driver.find_element(By.CSS_SELECTOR, ".bg-accent").text == "Delete Account"
    driver.find_element(By.CSS_SELECTOR, ".bg-accent").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".flex:nth-child(1) > .bg-accent")))
    driver.find_element(By.CSS_SELECTOR, ".flex:nth-child(1) > .bg-accent").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "login_button_navbar")))
    try:
        helper.login(driver, 999999901, "test5@email")
    except:
        ...
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.XPATH, '//h1[text()="Not authorized :c"]')))
    assert driver.find_element(By.XPATH, '//h1[text()="Not authorized :c"]').text == "Not authorized :c"

def test_change_location(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, "#dropdown-button > .flex")))
    driver.find_element(By.CSS_SELECTOR, "#dropdown-button > .flex").click()
    driver.find_element(By.LINK_TEXT, "Denmark").click()
    driver.find_element(By.ID, "save_settings_button").click()

    driver.find_element(By.LINK_TEXT, "Card Labs").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 30).until(expected_conditions.text_to_be_present_in_element((By.CSS_SELECTOR, "#dropdown-button > .flex"), "Denmark"))

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, "#dropdown-button > .flex")))
    driver.find_element(By.CSS_SELECTOR, "#dropdown-button > .flex").click()
    driver.find_element(By.LINK_TEXT, "Germany").click()
    driver.find_element(By.ID, "save_settings_button").click()

    driver.find_element(By.LINK_TEXT, "Card Labs").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".shadow")))
    driver.find_element(By.CSS_SELECTOR, ".shadow").click()
    driver.find_element(By.ID, "settings_button_navbar").click()

    WebDriverWait(driver, 30).until(expected_conditions.text_to_be_present_in_element((By.CSS_SELECTOR, "#dropdown-button > .flex"), "Germany"))

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, "#dropdown-button > .flex")))
    driver.find_element(By.CSS_SELECTOR, "#dropdown-button > .flex").click()
    driver.find_element(By.LINK_TEXT, "Austria").click()
    driver.find_element(By.ID, "save_settings_button").click()
