package com.uitesting.pages.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchPage {

    private static final By SEARCH_CONTAINER = AppiumBy.id("org.wikipedia:id/search_container");
    private static final By SEARCH_INPUT = AppiumBy.id("org.wikipedia:id/search_src_text");
    private static final By SEARCH_TAB = AppiumBy.id("org.wikipedia:id/nav_tab_search");
    private static final By SEARCH_CARD = AppiumBy.id("org.wikipedia:id/search_card");
    private static final By SEARCH_ACTION = AppiumBy.id("org.wikipedia:id/search_action");
    private static final By SEARCH_MENU = AppiumBy.id("org.wikipedia:id/menu_search");
    private static final By SEARCH_BAR = AppiumBy.id("org.wikipedia:id/search_bar");
    private static final By SEARCH_BAR_TEXT = AppiumBy.id("org.wikipedia:id/search_bar_text");
    private static final By SEARCH_ACCESSIBILITY = AppiumBy.accessibilityId("Search Wikipedia");
    private static final By RESULT_TITLE = AppiumBy.id("org.wikipedia:id/page_list_item_title");
    private static final By RESULT_DESCRIPTION = AppiumBy.id("org.wikipedia:id/page_list_item_description");
    private static final By CLEAR_SEARCH = AppiumBy.id("org.wikipedia:id/search_close_btn");

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public SearchPage(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void openSearch() {
        By toClick = wait.until(driver ->
                findFirstVisible(
                        SEARCH_CONTAINER,
                        SEARCH_TAB,
                        SEARCH_CARD,
                        SEARCH_ACTION,
                        SEARCH_MENU,
                        SEARCH_BAR,
                        SEARCH_BAR_TEXT,
                        SEARCH_ACCESSIBILITY,
                        AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"Search\")"),
                        AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Search\")")
                ).orElse(null)
        );

        if (toClick == null) {
            throw new NoSuchElementException("Не найден элемент для открытия поиска Wikipedia");
        }

        wait.until(ExpectedConditions.elementToBeClickable(toClick)).click();
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
    }

    public void typeQuery(String query) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
        input.clear();
        input.sendKeys(query);
    }

    public List<WebElement> resultTitles() {
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(RESULT_TITLE);
            return elements.isEmpty() ? null : elements;
        });
    }

    public int resultTitlesCount() {
        return driver.findElements(RESULT_TITLE).size();
    }

    public List<WebElement> resultDescriptions() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(RESULT_DESCRIPTION));
        return driver.findElements(RESULT_DESCRIPTION);
    }

    public String openFirstResult() {
        List<WebElement> titles = resultTitles();
        WebElement first = titles.get(0);
        String titleText = first.getText();
        first.click();
        return titleText;
    }

    public List<String> resultTitleTexts() {
        return resultTitles().stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public String openResultWithText(String text) {
        String lower = text.toLowerCase();
        for (int attempt = 0; attempt < 6; attempt++) {
            List<WebElement> titles = resultTitles();
            Optional<WebElement> match = titles.stream()
                    .filter(e -> {
                        String t = e.getText();
                        return t != null && t.toLowerCase().contains(lower);
                    })
                    .findFirst();

            if (match.isPresent()) {
                String titleText = match.get().getText();
                match.get().click();
                return titleText;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        List<WebElement> titles = resultTitles();
        WebElement first = titles.get(0);
        String titleText = first.getText();
        first.click();
        return titleText;
    }

    public void waitResultsDisappear() {
        wait.until(driver -> driver.findElements(RESULT_TITLE).isEmpty());
    }

    public void clearSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(CLEAR_SEARCH)).click();
    }

    private Optional<By> findFirstVisible(By... locators) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                return Optional.of(locator);
            }
        }
        return Optional.empty();
    }
}
