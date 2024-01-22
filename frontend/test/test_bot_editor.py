import helper
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
import re


def test_create_bot_and_check_template_code(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.ID, "button_create_new_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText").startswith("; It's your turn, select a card"))
    assert driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText").startswith("; It's your turn, select a card")

def test_create_bot_and_save_it(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.ID, "button_create_new_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys(Keys.CONTROL, "a") # mark code template
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys(Keys.BACKSPACE)  # delete code template
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys("test")
    driver.find_element(By.ID, "button_save_bot").click()
    WebDriverWait(driver, 30).until(lambda driver: re.match("^.*[/]bot[/]editor[/][0-9]+$", driver.current_url)) # wait for url change
    bot_url = driver.current_url
    driver.find_element(By.ID, "button_save_bot").click()
    driver.find_element(By.ID, "home_authenticated").click()
    driver.get(bot_url)
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText") == "test")
    assert driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText") == "test"


def test_create_bot_save_it_open_it_and_change_it_and_save_it(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.ID, "button_create_new_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys(Keys.CONTROL, "a")
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys("test")
    driver.find_element(By.ID, "button_save_bot").click()
    WebDriverWait(driver, 30).until(lambda driver: re.match("^.*[/]bot[/]editor[/][0-9]+$", driver.current_url)) # wait for url change
    bot_url = driver.current_url
    driver.find_element(By.ID, "button_save_bot").click()
    driver.find_element(By.ID, "home_authenticated").click()
    driver.get(bot_url)
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    assert driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText") == "test"
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText") == "test")
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys(Keys.CONTROL, "a")
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys("asdf")
    driver.find_element(By.ID, "button_save_bot").click()
    driver.find_element(By.ID, "home_authenticated").click()
    driver.get(bot_url)
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    assert driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText") == "asdf"
