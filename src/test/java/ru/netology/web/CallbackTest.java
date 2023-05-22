package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.Color;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

//========= Драйвер для Microsoft Edge (не работает с Appveyor !!!) =========

//    @BeforeAll
//    static void setUpAll() {
//        WebDriverManager.edgedriver().setup();
//    }
//
//    @BeforeEach
//    void setUp() {
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
//        driver = new EdgeDriver(options);
//        driver.get("http://localhost:9999");
//    }

//============================================================================

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }


    // 1) Тест на корректный заказ карты

    @Test
    void shouldCardOrder() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button_view_extra")).click();
        WebElement success = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = success.getText().trim();
        assertEquals(expected, actual);
    }

    // 2) Тест, если неверно заполнено поле "Фамилия и имя"

    @Test
    void shouldInvalidCardFio() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Alexandr");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button_view_extra")).click();
        WebElement invalid = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = invalid.getText().trim();
        assertEquals(expected, actual);
    }

    // 3) Тест, если не заполнено поле "Фамилия и имя"

    @Test
    void shouldInvalidCardFioEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button_view_extra")).click();
        WebElement invalid = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String expected = "Поле обязательно для заполнения";
        String actual = invalid.getText().trim();
        assertEquals(expected, actual);
    }

    // 4) Тест, если неверно заполнено поле "Телефон"

    @Test
    void shouldInvalidCardPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("9999999");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button_view_extra")).click();
        WebElement invalid = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = invalid.getText().trim();
        assertEquals(expected, actual);
    }

    // 5) Тест, если не заполнено поле "Телефон"

    @Test
    void shouldInvalidCardPhoneEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button_view_extra")).click();
        WebElement invalid = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String expected = "Поле обязательно для заполнения";
        String actual = invalid.getText().trim();
        assertEquals(expected, actual);
    }

    // 6) Тест, если не нажат чекбокс (текст красным цветом)

    @Test
    void shouldCheckboxNotChecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("button.button_view_extra")).click();
        WebElement invalid = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text"));
        String color = invalid.getCssValue("color");
        String expected = Color.fromString(color).asHex();
        String actual = "#ff5c5c";
        assertEquals(expected, actual);
    }
}

