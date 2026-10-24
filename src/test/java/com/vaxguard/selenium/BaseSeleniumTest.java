package com.vaxguard.selenium;

import com.vaxguard.VaxGuardApplication;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(classes = VaxGuardApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(ScreenshotOnFailureExtension.class)
public abstract class BaseSeleniumTest {

    @LocalServerPort
    protected int port;

    protected WebDriver driver;
    protected WebDriverWait wait;

    public WebDriver getDriver() {
        return driver;
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    public void setUp() {
        boolean headless = Boolean.parseBoolean(System.getProperty("selenium.headless", "true"));
        boolean chromeAvailable = isChromeAvailable();

        try {
            if (chromeAvailable) {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu",
                        "--window-size=1920,1080", "--remote-allow-origins=*");
                driver = new ChromeDriver(options);
            } else {
                File localDriver = new File(".tools/edgedriver/msedgedriver.exe");
                if (localDriver.exists()) {
                    System.setProperty("webdriver.edge.driver", localDriver.getAbsolutePath());
                }
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu",
                        "--window-size=1920,1080", "--remote-allow-origins=*");
                driver = new EdgeDriver(options);
            }
        } catch (Throwable t) {
            System.err.println("[SELENIUM WARN] Browser driver initialization failed or offline: " + t.getMessage());
        }

        org.junit.jupiter.api.Assumptions.assumeTrue(driver != null,
                "Selenium browser driver is not available in the current environment; skipping browser test.");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private boolean isChromeAvailable() {
        String[] chromePaths = {
                "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",
                "/usr/bin/google-chrome",
                "/usr/bin/chromium-browser"
        };
        for (String p : chromePaths) {
            if (new File(p).exists()) {
                return true;
            }
        }
        return false;
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
        }
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement el = waitForVisible(locator);
        String typeAttr = el.getAttribute("type");
        if ("date".equalsIgnoreCase(typeAttr)) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];" +
                    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                    "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    el, text);
        } else {
            el.clear();
            el.sendKeys(text);
        }
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText();
    }
}
