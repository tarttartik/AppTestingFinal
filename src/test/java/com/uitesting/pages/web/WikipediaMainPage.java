package com.uitesting.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WikipediaMainPage extends BaseWebPage {

    private static final By PAGE_BODY = By.id("www-wikipedia-org");
    private static final By WELCOME_MESSAGE = By.id("mp-welcome");
    private static final By SEARCH_INPUT = By.id("searchInput");
    private static final By SEARCH_BUTTON = By.id("searchButton");
    private static final By RANDOM_ARTICLE_LINK = By.id("n-randompage");
    private static final By FEATURED_ARTICLE_TITLE = By.cssSelector("#mp-tfa b a");

    public WikipediaMainPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isWelcomeMessageVisible() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.id("mp-topbanner")),
                ExpectedConditions.presenceOfElementLocated(PAGE_BODY)
        ));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(WELCOME_MESSAGE)).isDisplayed();
    }

    public WikipediaArticlePage searchFor(String term) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
        input.clear();
        input.sendKeys(term);
        input.sendKeys(Keys.ENTER);
        return new WikipediaArticlePage(driver, wait);
    }

    public WikipediaArticlePage openRandomArticle() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(RANDOM_ARTICLE_LINK)).click();
            return new WikipediaArticlePage(driver, wait);
        } catch (TimeoutException e) {
            // Fallback if sidebar is hidden (e.g., narrow layout or banner overlays).
            driver.navigate().to("https://en.wikipedia.org/wiki/Special:Random");
            return new WikipediaArticlePage(driver, wait);
        }
    }

    public String featuredArticleTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(FEATURED_ARTICLE_TITLE)).getText();
    }
}
