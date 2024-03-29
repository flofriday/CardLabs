import helper
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions
import time


def test_delete_bot_from_editor_abort_with_go_back_button(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    helper.new_bot(driver, "test code")

    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText").startswith("test code; It's your turn, select a card"))
    botname = driver.find_element(By.ID, "leftHeading").text
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "button_delete_bot")))
    driver.find_element(By.ID, "button_delete_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "delete_modal_back_button")))
    assert driver.find_element(By.ID, "leftHeading").text == botname

def test_delete_bot_from_editor_abort_with_click_on_background_button(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    helper.new_bot(driver, "test code")

    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText").startswith("test code; It's your turn, select a card"))
    botname = driver.find_element(By.ID, "leftHeading").text
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "button_delete_bot")))
    driver.find_element(By.ID, "button_delete_bot").click()
    driver.find_element(By.CSS_SELECTOR, ".z-50").click()
    assert driver.find_element(By.ID, "leftHeading").text == botname


def test_delete_bot_from_editor_abort_with_go_cross_button(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    helper.new_bot(driver, "test code")

    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText").startswith("test code; It's your turn, select a card"))
    botname = driver.find_element(By.ID, "leftHeading").text
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "button_delete_bot")))
    driver.find_element(By.ID, "button_delete_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".-right-3 > svg")))
    driver.find_element(By.CSS_SELECTOR, ".-right-3 > svg").click()
    assert driver.find_element(By.ID, "leftHeading").text == botname

def test_delete_bot_from_editor_should_navigate_to_my_bots(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    helper.new_bot(driver, "test code")

    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.CSS_SELECTOR, ".cm-content").get_attribute("innerText").startswith("test code; It's your turn, select a card"))
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "button_delete_bot")))
    driver.find_element(By.ID, "button_delete_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "delete_modal_delete_button")))
    driver.find_element(By.ID, "delete_modal_delete_button").click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.ID, "leftHeading").text == "My Bots")
    assert driver.find_element(By.ID, "leftHeading").text == "My Bots"

def test_delete_bot_from_bot_overview_abort_with_go_back_button(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")

    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.LINK_TEXT, "My Bots")))
    driver.find_element(By.LINK_TEXT, "My Bots").click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.ID, "leftHeading").text == "My Bots")
    botname = driver.find_element(By.CSS_SELECTOR, ".relative:nth-child(1) > .text-text").text
    driver.find_element(By.CSS_SELECTOR, ".relative:nth-child(1) path").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "delete_modal_back_button")))
    driver.find_element(By.ID, "delete_modal_back_button").click()
    assert driver.find_element(By.CSS_SELECTOR, ".relative:nth-child(1) > .text-text").text == botname

def test_delete_bot_from_bot_overview_abort_with_go_cross_button(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")

    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.LINK_TEXT, "My Bots")))
    driver.find_element(By.LINK_TEXT, "My Bots").click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.ID, "leftHeading").text == "My Bots")
    botname = driver.find_element(By.CSS_SELECTOR, ".relative:nth-child(1) > .text-text").text
    driver.find_element(By.CSS_SELECTOR, ".relative:nth-child(1) path").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".-right-3 > svg")))
    driver.find_element(By.CSS_SELECTOR, ".-right-3 > svg").click()
    assert driver.find_element(By.CSS_SELECTOR, ".relative:nth-child(1) > .text-text").text == botname

def test_delete_bot_from_bot_overview(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")

    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.LINK_TEXT, "My Bots")))
    driver.find_element(By.LINK_TEXT, "My Bots").click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.ID, "leftHeading").text == "My Bots")
    botname = driver.find_element(By.CSS_SELECTOR, ".relative:nth-child(1) > .text-text").text
    driver.find_element(By.CSS_SELECTOR, ".relative:nth-child(1) path").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.ID, "delete_modal_delete_button")))
    driver.find_element(By.ID, "delete_modal_delete_button").click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.ID, "leftHeading").text == "My Bots")
    assert driver.find_element(By.CSS_SELECTOR, "div.grid > div:nth-child(1)").text != botname
