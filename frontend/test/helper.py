

from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait

def login(driver, username, password):
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "login_button_navbar")))
    driver.find_element(By.ID, "login_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys(username)
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys(password)
    driver.find_element(By.ID, "login_button").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "profile_pic_in_navbar")))


def register(driver, username, email, password, location):
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "register_button_navbar")))
    driver.find_element(By.ID, "register_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys(username)
    driver.find_element(By.ID, "email").send_keys(email)
    driver.find_element(By.CSS_SELECTOR, ".ml-2").click()
    driver.find_element(By.LINK_TEXT, location).click()
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys(password)
    driver.find_element(By.ID, "register_button").click()
