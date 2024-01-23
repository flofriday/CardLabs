import helper
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.action_chains import ActionChains
import re
import time


def test_create_bot_and_try_to_rank(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.ID, "button_create_new_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "button_rank_bot")))
    driver.find_element(By.ID, "button_rank_bot").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".text-sm > div:nth-child(2)")))
    assert driver.find_element(By.CSS_SELECTOR, ".text-sm > div:nth-child(2)").get_attribute("innerText") == "Bot needs to be saved before it can be ranked"


def test_code_editor_save_and_rank(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.ID, "button_create_new_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys("my bot")
    # TODO: it should not be necessary to do this two times
    for i in range (0,2):
        WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "button_save_bot")))
        driver.find_element(By.ID, "button_save_bot").click()
        WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".text-sm > div:nth-child(2)")))
        driver.find_element(By.CSS_SELECTOR, ".text-sm > div:nth-child(2)").click()
        WebDriverWait(driver, 5).until_not(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".text-sm > div:nth-child(2)")))
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "button_rank_bot")))
    driver.find_element(By.ID, "button_rank_bot").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".text-sm > div:nth-child(2)")))
    assert driver.find_element(By.CSS_SELECTOR, ".text-sm > div:nth-child(2)").get_attribute("innerText") == "This bot has been queued for ranking"
