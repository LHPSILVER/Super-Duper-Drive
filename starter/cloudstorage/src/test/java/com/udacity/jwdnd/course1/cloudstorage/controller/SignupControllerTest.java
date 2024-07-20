package com.udacity.jwdnd.course1.cloudstorage.controller;

import static org.junit.jupiter.api.Assertions.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class SignupControllerTest {
  private WebDriver driver;

  public SignupControllerTest() {}

  @BeforeEach
  public void initWebDriver() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.get("http://localhost:8080/login");
    driver.findElement(By.id("signup")).click();
    driver.findElement(By.id("inputFirstName")).clear();
    driver.findElement(By.id("inputLastName")).clear();
    driver.findElement(By.id("inputUsername")).clear();
    driver.findElement(By.id("inputPassword")).clear();
  }

  @AfterEach
  public void closeWebDriver() {
    driver.quit();
  }

  @Test
  public void check_if_user_is_created_successfully() {
    createUserAction();
    assertEquals("http://localhost:8080/files", driver.getCurrentUrl());
  }

  @Test
  public void check_if_logout_is_successfully() {
    createUserAction();
    driver.findElement(By.cssSelector("#logout-btn")).click();
    assertEquals("http://localhost:8080/login", driver.getCurrentUrl());
    assertNotNull(driver.findElement(By.cssSelector("#logout-msg")));
  }

  public void createUserAction() {
    driver.findElement(By.id("inputFirstName")).sendKeys("Pham");
    driver.findElement(By.id("inputLastName")).sendKeys("Dat");
    driver.findElement(By.id("inputUsername")).sendKeys("DatPX");
    driver.findElement(By.id("inputPassword")).sendKeys("fpt@12345");
    driver.findElement(By.id("buttonSignUp")).click();
    driver.findElement(By.id("to-login-page")).click();
    driver.findElement(By.id("inputUsername")).clear();
    driver.findElement(By.id("inputPassword")).clear();
    driver.findElement(By.id("inputUsername")).sendKeys("DatPX");
    driver.findElement(By.id("inputPassword")).sendKeys("fpt@12345");
    driver.findElement(By.id("login-button")).click();
  }
}
