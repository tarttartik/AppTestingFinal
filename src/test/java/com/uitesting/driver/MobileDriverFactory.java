package com.uitesting.driver;

import com.uitesting.config.TestConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.jdk.JdkHttpClient;
import org.openqa.selenium.WebDriverException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class MobileDriverFactory {

    private MobileDriverFactory() {
    }

    public static AndroidDriver createAndroidDriver() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName(TestConfig.deviceName());
        options.setUdid(TestConfig.deviceName());
        if (!TestConfig.platformVersion().isEmpty()) {
            options.setPlatformVersion(TestConfig.platformVersion());
        }
        options.setAppPackage("org.wikipedia");
        options.setAppActivity("org.wikipedia.main.MainActivity");
        options.setNoReset(true);
        options.setNewCommandTimeout(Duration.ofSeconds(120));
        options.setAutoGrantPermissions(true);
        options.setCapability("forceAppLaunch", true);

        String baseUrl = TestConfig.appiumServerUrl();
        try {
            return new AndroidDriver(new URL(baseUrl), options);
        } catch (WebDriverException first) {
            // Appium 2/3 по умолчанию слушает без /wd/hub; пробуем альтернативный путь.
            String fallbackUrl = toggleHubPath(baseUrl);
            if (fallbackUrl.equals(baseUrl)) {
                throw first;
            }
            try {
                return new AndroidDriver(new URL(fallbackUrl), options);
            } catch (MalformedURLException ignored) {
                throw first;
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Appium server URL: " + baseUrl, e);
        }
    }

    private static String toggleHubPath(String url) {
        if (url.endsWith("/wd/hub")) {
            return url.replace("/wd/hub", "");
        }
        if (url.endsWith("/")) {
            return url + "wd/hub";
        }
        return url + "/wd/hub";
    }
}
