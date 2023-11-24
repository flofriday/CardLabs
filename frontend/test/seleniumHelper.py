from selenium import webdriver
from selenium_driver_updater import DriverUpdater
from selenium.webdriver.chrome.service import Service as ChromeService
import os
import pytest


def get_options():
    options = webdriver.ChromeOptions()
    options.add_argument('--disable-gpu')
    options.add_experimental_option('excludeSwitches', ['enable-logging'])
    options.add_argument("--log-level=3")
    return options

def get_service():
    base_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), "chromedriver")
    filename = DriverUpdater.install(path=base_dir, driver_name=DriverUpdater.chromedriver, upgrade=True, check_driver_is_up_to_date=True)
    service = ChromeService(executable_path=filename)
    return service

@pytest.fixture(scope="session")
def driver():
    driver = webdriver.Chrome(options=get_options(), service=get_service())
    yield driver

@pytest.fixture(scope="session")
def driver_headless():
    options = get_options()
    options.add_argument('--headless=new')

    driver_headless = webdriver.Chrome(options=options, service=get_service())
    yield driver_headless
