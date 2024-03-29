from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
import re
import jwt
from datetime import datetime, timedelta
import base64
import time



def login(driver, id, email):
    driver.get("http://127.0.0.1:3000/")
    driver.set_window_size(1900, 1020)

    jwt.api_jws.PyJWS.header_typ = False
    secret = "2e0377d9c56d8a51ed8cfc10a68bda5b3cdc7453a6c7b6ac4728d93e052618051bba4534b146e3cff10bed31224793cb46f78a628b8d6dc08b1b5496a05cf488"

    current_time = datetime.now()
    expiration_time = current_time + timedelta(minutes=15)

    payload = {
        "iat": int(current_time.timestamp()),
        "exp": int(expiration_time.timestamp()),
        "sub": email,
        "account-id": id,
        "account-email": email,
        "token-type": "ACCESS_TOKEN"
    }

    token = jwt.encode(payload, base64.b64decode(secret), algorithm="HS512", headers={"alg" : "HS512"})
    driver.add_cookie({"name": "auth_token", "value": token})

    script = f"""
    localStorage.setItem('auth_token_expire', '{expiration_time.strftime("%Y-%m-%dT%H:%M:%SZ")}');
    localStorage.setItem('refresh_token', 'test_token');
    """
    driver.execute_script(script)

    driver.get("http://127.0.0.1:3000/dashboard")

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


def new_bot(driver, code):
    driver.find_element(By.ID, "button_create_new_bot").click()
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))
    driver.find_element(By.CSS_SELECTOR, ".cm-content").send_keys(code)
    driver.find_element(By.ID, "button_save_bot").click()
    WebDriverWait(driver, 30).until(lambda driver: re.match("^.*[/]bot[/]editor[/][0-9]+$", driver.current_url)) # wait for url change
    bot_url = driver.current_url
    driver.find_element(By.ID, "button_save_bot").click()
    driver.find_element(By.ID, "home_authenticated").click()
    driver.get(bot_url)
    WebDriverWait(driver, 30).until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, ".cm-content")))

def get_code_template():
    return """; It's your turn, select a card
; hand is just a list of cards
; topCard is a single card
(define turn (lambda (topCard hand players)
    (random-choice
        (matching-cards topCard hand))))

; Event: cardPlayed
; UnoSaid event is not provided as you can just use cardPlayed
(define cardPlayed (lambda (card player)
    ))

; Event: cardPicked
; Some other player had to pick a card
(define cardPicked (lambda (topCard player)
    ))

; Event: reshuffledPile
; The drawPile got reshuffled
(define reshuffledPile (lambda ()
    ))"""
