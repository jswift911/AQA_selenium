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

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }


    // 1) Тест на корректный заказ карты

    @Test
    void shouldCardOrder() {
        List<WebElement> elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Александр");
        elements.get(1).sendKeys("+79270000000");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.className("paragraph")).getText().trim();
        assertEquals(expected, actual);
    }

    // 2) Тест, если неверно заполнено поле "Фамилия и имя"

    @Test
    void shouldInvalidCardFio() {
        List<WebElement> elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Alexandr");
        driver.findElement(By.className("button")).click();
        WebElement invalid = driver.findElement(By.className("input_invalid"));
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = invalid.findElement(By.className("input__sub")).getText().trim();;
        assertEquals(expected, actual);
    }

    // 3) Тест, если не заполнено поле "Фамилия и имя"

    @Test
    void shouldInvalidCardFioEmpty() {
        List<WebElement> elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("");
        driver.findElement(By.className("button")).click();
        WebElement invalid = driver.findElement(By.className("input_invalid"));
        String expected = "Поле обязательно для заполнения";
        String actual = invalid.findElement(By.className("input__sub")).getText().trim();;
        assertEquals(expected, actual);
    }

    // 4) Тест, если неверно заполнено поле "Телефон"

    @Test
    void shouldInvalidCardPhone() {
        List<WebElement> elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Александр");
        elements.get(1).sendKeys("9999999");
        driver.findElement(By.className("button")).click();
        WebElement invalid = driver.findElement(By.className("input_invalid"));
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = invalid.findElement(By.className("input__sub")).getText().trim();;
        assertEquals(expected, actual);
    }

    // 5) Тест, если не заполнено поле "Телефон"

    @Test
    void shouldInvalidCardPhoneEmpty() throws InterruptedException {
        List<WebElement> elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Александр");
        elements.get(1).sendKeys(" ");
        driver.findElement(By.className("button")).click();
        WebElement invalid = driver.findElement(By.className("input_invalid"));
        String expected = "Поле обязательно для заполнения";
        String actual = invalid.findElement(By.className("input__sub")).getText().trim();;
        assertEquals(expected, actual);
    }
}

