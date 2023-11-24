

from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait

def login(driver, username, password):
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "login_button_navbar")))
    driver.find_element(By.ID, "login_button_navbar").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "username")))
    driver.find_element(By.ID, "username").click()
    driver.find_element(By.ID, "username").send_keys(username)
    driver.find_element(By.ID, "password").click()
    driver.find_element(By.ID, "password").send_keys(password)
    driver.find_element(By.ID, "login_button").click()
    WebDriverWait(driver, 5).until(expected_conditions.presence_of_element_located((By.ID, "profile_pic_in_navbar")))
