package com.vaxguard.selenium;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ScreenshotOnFailureExtension implements TestWatcher {

    private static final String SCREENSHOT_DIR = "target/screenshots";

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Object testInstance = context.getRequiredTestInstance();
        if (testInstance instanceof BaseSeleniumTest) {
            WebDriver driver = ((BaseSeleniumTest) testInstance).getDriver();
            if (driver != null && driver instanceof TakesScreenshot) {
                try {
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    Path targetDir = Paths.get(SCREENSHOT_DIR);
                    if (!Files.exists(targetDir)) {
                        Files.createDirectories(targetDir);
                    }

                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                    String className = context.getRequiredTestClass().getSimpleName();
                    String methodName = context.getRequiredTestMethod().getName();
                    String fileName = String.format("%s_%s_FAILURE_%s.png", className, methodName, timestamp);

                    Path destination = targetDir.resolve(fileName);
                    Files.copy(screenshot.toPath(), destination);
                    System.out.println("[SELENIUM QUALITY GATE] Test failure screenshot captured: " + destination.toAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Failed to capture screenshot: " + e.getMessage());
                }
            }
        }
    }
}
