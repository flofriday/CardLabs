import helper
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
import re

def test_create_bot_and_save_it(driver_headless):
    driver = driver_headless
    helper.login(driver, "test1", "pass1")
    driver.find_element(By.ID, "button_create_new_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".view-lines")))
    driver.find_element(By.CSS_SELECTOR, ".monaco-editor textarea").send_keys("test")
    driver.find_element(By.ID, "button_save_bot").click()
    WebDriverWait(driver, 30).until(lambda driver: re.match("^.*[/]bot[/]editor[/][0-9]+$", driver.current_url)) # wait for url change
    bot_url = driver.current_url
    driver.find_element(By.ID, "home_authenticated").click()
    driver.get(bot_url)
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".view-lines")))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".view-lines").text == "test")
    assert driver.find_element(By.CSS_SELECTOR, ".view-lines").text == "test"


def test_create_bot_save_it_open_it_and_change_it_and_save_it(driver_headless):
    driver = driver_headless
    helper.login(driver, "test1", "pass1")
    driver.find_element(By.ID, "button_create_new_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".view-lines")))
    driver.find_element(By.CSS_SELECTOR, ".monaco-editor textarea").send_keys("test")
    driver.find_element(By.ID, "button_save_bot").click()
    WebDriverWait(driver, 30).until(lambda driver: re.match("^.*[/]bot[/]editor[/][0-9]+$", driver.current_url)) # wait for url change
    bot_url = driver.current_url
    driver.find_element(By.ID, "home_authenticated").click()
    driver.get(bot_url)
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".monaco-editor textarea")))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".view-lines").text == "test")
    driver.find_element(By.CSS_SELECTOR, ".monaco-editor textarea").send_keys(Keys.CONTROL, "a")
    driver.find_element(By.CSS_SELECTOR, ".monaco-editor textarea").send_keys("asdf")
    driver.find_element(By.ID, "button_save_bot").click()
    driver.find_element(By.ID, "home_authenticated").click()
    driver.get(bot_url)
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".view-lines")))
    assert driver.find_element(By.CSS_SELECTOR, ".view-lines").text == "asdf"
