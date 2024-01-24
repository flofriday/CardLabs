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
    assert driver.find_element(By.CSS_SELECTOR, ".text-sm > div:nth-child(2)").get_attribute("innerText") == "The bot needs to be saved before ranking"
    time.sleep(1)
    actions = ActionChains(driver) 
    actions.move_to_element(driver.find_element(By.CSS_SELECTOR, ".text-sm > div:nth-child(2)")) 
    actions.click() 
    actions.perform()
    time.sleep(2)

    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "button_save_bot")))
    driver.find_element(By.ID, "button_save_bot").click()

    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".text-sm > div:nth-child(2)")))
    assert driver.find_element(By.CSS_SELECTOR, ".text-sm > div:nth-child(2)").get_attribute("innerText") == "Bot created and saved!"
    time.sleep(1)
    actions = ActionChains(driver) 
    actions.move_to_element(driver.find_element(By.CSS_SELECTOR, ".text-sm > div:nth-child(2)")) 
    actions.click() 
    actions.perform()
    time.sleep(2)

    driver.find_element(By.ID, "button_rank_bot").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".text-sm > div:nth-child(2)")))
    assert driver.find_element(By.CSS_SELECTOR, ".text-sm > div:nth-child(2)").get_attribute("innerText") == "Successfully queued the bot for ranking"
