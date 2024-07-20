package com.udacity.jwdnd.course1.cloudstorage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class LoginControllerTest {
  private WebDriver driver;

  public LoginControllerTest() {}

  @BeforeEach
  public void init() throws InterruptedException {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.get("http://localhost:8080/login");
    Thread.sleep(1000);
  }

  @AfterEach
  public void close() {
    driver.quit();
  }

  @Test
  public void unauthorized_user_can_not_access_home_page() throws InterruptedException {
    driver.get("http://localhost:8080/files");
    assertEquals("http://localhost:8080/login", driver.getCurrentUrl());
    Thread.sleep(2000);
  }

  @Test
  public void unauthorized_user_can_access_login_page() throws InterruptedException {
    assertEquals("http://localhost:8080/login", driver.getCurrentUrl());
    Thread.sleep(2000);
  }

  @Test
  public void unauthorized_user_can_access_signup_page() throws InterruptedException {
    driver.get("http://localhost:8080/signup");
    assertEquals("http://localhost:8080/signup", driver.getCurrentUrl());
    Thread.sleep(2000);
  }
}
