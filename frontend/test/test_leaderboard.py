import pytest
import helper
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait

def test_logedin_leaderboard_default(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.ID, "leaderboard_link")))
    driver.find_element(By.ID, "leaderboard_link").click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="1." and p/text()="SecondNewPagius"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="1." and p/text()="Zargitron"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="1." and p/text()="Zargitron"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="4."]'))

def test_logedout_leaderboard_default(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.ID, "leaderboard_link")))
    driver.find_element(By.ID, "leaderboard_link").click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="1." and p/text()="SecondNewPagius"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="1." and p/text()="Zargitron"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="1." and p/text()="Zargitron"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="4."]'))

def test_logedout_leaderboard_switch_page(driver_headless):
    driver = driver_headless
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.ID, "leaderboard_link")))
    driver.find_element(By.ID, "leaderboard_link").click()
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.XPATH, '//button[text()="2"]')))
    driver.find_element(By.XPATH, '//button[text()="2"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="7." and p/text()="Tynixos"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="8." and p/text()="Xendorphis"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="8." and p/text()="Jenphoos"]'))

def test_logedin_leaderboard_switch_page(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.ID, "leaderboard_link")))
    driver.find_element(By.ID, "leaderboard_link").click()
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.XPATH, '//button[text()="2"]')))
    driver.find_element(By.XPATH, '//button[text()="2"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="7." and p/text()="Tynixos"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="8." and p/text()="Xendorphis"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="8." and p/text()="Jenphoos"]'))

def test_logedin_region_selector_continent(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999997, "test3@email")
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.ID, "leaderboard_link")))
    driver.find_element(By.ID, "leaderboard_link").click()
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.XPATH, '//button[text()="Continent"]')))
    driver.find_element(By.XPATH, '//button[text()="Continent"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="1." and p/text()="Neotarphis"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="2." and p/text()="Krylonium"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="3." and p/text()="Neophis"]'))

def test_logedin_region_selector_country(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999998, "test2@email")
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.ID, "leaderboard_link")))
    driver.find_element(By.ID, "leaderboard_link").click()
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.XPATH, '//button[text()="Country"]')))
    driver.find_element(By.XPATH, '//button[text()="Country"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="1." and p/text()="Jenphoium"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="2." and p/text()="Zarlonphis"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="3." and p/text()="Zarzorax"]'))

def test_logedin_myleaderboard_global(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999998, "test2@email")
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.ID, "myleaderboard_link")))
    driver.find_element(By.ID, "myleaderboard_link").click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="10." and p/text()="Jenphoium"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="16." and p/text()="Zarlonphis"]'))
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="28." and p/text()="Zarzorax"]'))

def test_logedin_myleaderboard_switch_page(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.ID, "myleaderboard_link")))
    driver.find_element(By.ID, "myleaderboard_link").click()
    WebDriverWait(driver, 10).until(expected_conditions.presence_of_element_located((By.XPATH, '//button[text()="3"]')))
    driver.find_element(By.XPATH, '//button[text()="3"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[p/text()="28." and p/text()="Neous"]'))


def test_logedin_dashboard_myleaderboard_continent(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.XPATH, '//div[starts-with(div/div/div/h2/text(), "My Bots")]/ul/li/a[text()="Continent"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[h2/text()="My Bots - Continent"]/div/div[p/text()="1. Zargitron"]'))

def test_logedin_dashboard_myleaderboard_country(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.XPATH, '//div[starts-with(div/div/div/h2/text(), "My Bots")]/ul/li/a[text()="Country"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[h2/text()="My Bots - Country"]/div/div[p/text()="1. Zargitron"]'))

def test_logedin_dashboard_myleaderboard_global(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.XPATH, '//div[starts-with(div/div/div/h2/text(), "My Bots")]/ul/li/a[text()="Global"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[h2/text()="My Bots - Global"]/div/div[p/text()="1. Zargitron"]'))

def test_logedin_dashboard_globalleaderboard_continent(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.XPATH, '//div[contains(div/div/div/h2/text(), "Leaderboard")]/ul/li/a[text()="Continent"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[h2/text()="Continent Leaderboard"]/div/div[p/text()="1. Zargitron"]'))

def test_logedin_dashboard_globalleaderboard_country(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.XPATH, '//div[contains(div/div/div/h2/text(), "Leaderboard")]/ul/li/a[text()="Country"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[h2/text()="Country Leaderboard"]/div/div[p/text()="1. Zargitron"]'))

def test_logedin_dashboard_globalleaderboard_global(driver_headless):
    driver = driver_headless
    helper.login(driver, 999999999, "test1@email")
    driver.find_element(By.XPATH, '//div[contains(div/div/div/h2/text(), "Leaderboard")]/ul/li/a[text()="Global"]').click()
    WebDriverWait(driver, 30).until(lambda driver: driver.find_element(By.XPATH, '//div[h2/text()="Global Leaderboard"]/div/div[p/text()="1. Zargitron"]'))
