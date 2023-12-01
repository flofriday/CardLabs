# Generated by Selenium IDE
import pytest
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from random import randint

def test_register_with_valid_data(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "register_button_navbar")))
    driver.find_element(By.ID, "register_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys("test_register" + str(randint(0,99999999999)))
    driver.find_element(By.ID, "email").click()
    driver.find_element(By.ID, "email").send_keys("test@email" + str(randint(0,99999999999)) + ".com")
    driver.find_element(By.CSS_SELECTOR, ".ml-2").click()
    driver.find_element(By.LINK_TEXT, "Switzerland").click()
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys("Password1!")
    driver.find_element(By.ID, "register_button").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "profile_pic_in_navbar")))

def test_register_with_invalid_username(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "register_button_navbar")))
    driver.find_element(By.ID, "register_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys(" ")
    driver.find_element(By.ID, "email").click()
    driver.find_element(By.ID, "email").send_keys("test@email" + str(randint(0,99999999999)) + ".com")
    driver.find_element(By.CSS_SELECTOR, ".ml-2").click()
    driver.find_element(By.LINK_TEXT, "Switzerland").click()
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys("Password1!")
    driver.find_element(By.ID, "register_button").click()

    valdiationMessage = driver.execute_script("return arguments[0].validationMessage", driver.find_element(By.ID, "username"))
    assert valdiationMessage == "Username can't contain whitespaces"


def test_register_with_invalid_whitespace_password(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "register_button_navbar")))
    driver.find_element(By.ID, "register_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys("testuser" + str(randint(0,99999999999)))
    driver.find_element(By.ID, "email").click()
    driver.find_element(By.ID, "email").send_keys("test@email" + str(randint(0,99999999999)) + ".com")
    driver.find_element(By.CSS_SELECTOR, ".ml-2").click()
    driver.find_element(By.LINK_TEXT, "Switzerland").click()
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys("              ")
    driver.find_element(By.ID, "register_button").click()

    valdiationMessage = driver.execute_script("return arguments[0].validationMessage", driver.find_element(By.ID, "password"))
    assert valdiationMessage == "Password can't contain whitespaces"


def test_register_with_too_short_password(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "register_button_navbar")))
    driver.find_element(By.ID, "register_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys("testuser" + str(randint(0,99999999999)))
    driver.find_element(By.ID, "email").click()
    driver.find_element(By.ID, "email").send_keys("test@email" + str(randint(0,99999999999))  + ".com")
    driver.find_element(By.CSS_SELECTOR, ".ml-2").click()
    driver.find_element(By.LINK_TEXT, "Switzerland").click()
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys("a")
    driver.find_element(By.ID, "register_button").click()

    valdiationMessage = driver.execute_script("return arguments[0].validationMessage", driver.find_element(By.ID, "password"))
    assert valdiationMessage == "Password need to be at least 8 characters long"

def test_register_with_whitespace_email(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "register_button_navbar")))
    driver.find_element(By.ID, "register_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys("testuser" + str(randint(0,99999999999)))
    driver.find_element(By.ID, "email").click()
    driver.find_element(By.ID, "email").send_keys(" ")
    driver.find_element(By.CSS_SELECTOR, ".ml-2").click()
    driver.find_element(By.LINK_TEXT, "Switzerland").click()
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys("Password1!")
    driver.find_element(By.ID, "register_button").click()

    valdiationMessage = driver.execute_script("return arguments[0].validationMessage", driver.find_element(By.ID, "email"))
    assert valdiationMessage == "Please fill in this field."

def test_register_with_invalid_email(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "register_button_navbar")))
    driver.find_element(By.ID, "register_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys("testuser" + str(randint(0,99999999999)))
    driver.find_element(By.ID, "email").click()
    driver.find_element(By.ID, "email").send_keys("asdf@asdf")
    driver.find_element(By.CSS_SELECTOR, ".ml-2").click()
    driver.find_element(By.LINK_TEXT, "Switzerland").click()
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys("Password1!")
    driver.find_element(By.ID, "register_button").click()

    valdiationMessage = driver.execute_script("return arguments[0].validationMessage", driver.find_element(By.ID, "email"))
    assert valdiationMessage == "Invalid E-Mail address"
