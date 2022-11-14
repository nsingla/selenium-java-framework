package io.nsingla.selenium.extensions;

import io.nsingla.selenium.SeleniumBase;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScreenshotExtension implements AfterEachCallback {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotExtension.class);

    /**
     * Reads the byte arrays of the input {@code screenshotUrl}
     *
     * @param screenshotUrl The text of the screenshot url
     * @param fileName      The name of the file
     * @return byte
     */
    @Attachment(value = "{1}", type = "image/png")
    public static byte[] saveImageAttach(String screenshotUrl, String fileName) {
        try {
            return toByteArray(screenshotUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static byte[] toByteArray(String uri) throws IOException {
        return Files.readAllBytes(Paths.get(uri));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            WebDriver driver = ((SeleniumBase) context.getTestInstance()
                .orElseThrow(() -> new RuntimeException("Web driver is not present"))).getDriver();
            String className = context.getTestClass()
                .orElseThrow(() -> new RuntimeException("Class canonical name could not be set")).getCanonicalName();
            String methodName = context.getDisplayName();
            File screenshot;
            try {
                screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            } catch (UnhandledAlertException e) {
                Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                alert.accept();
                logger.debug("Alert appeared during screenshot creation, it will get accepted. Alert text: {}", alertText);
                screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            }
            File screenShotDir = new File("." + File.separator + "screenshots");
            if (!screenShotDir.exists()) {
                boolean screenShotDirCreated = screenShotDir.mkdirs();
                logger.info("Directory for download on location {} has been created (y/n? {}).", screenshot, screenShotDirCreated);
            }
            String filename = screenShotDir + File.separator + "screenshot-" + className + "-" + methodName + ".png";
            FileUtils.copyFile(screenshot, new File(filename));
            saveImageAttach(screenshot.getPath(), className + "." + methodName);
        }
    }

}
