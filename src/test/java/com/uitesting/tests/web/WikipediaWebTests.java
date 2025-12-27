package com.uitesting.tests.web;

import com.uitesting.pages.web.WikipediaArticlePage;
import com.uitesting.pages.web.WikipediaMainPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WikipediaWebTests extends BaseWebTest {

    @Test(groups = "web")
    public void openEnglishMainPageShowsWelcomeBlock() {
        WikipediaMainPage mainPage = new WikipediaMainPage(driver, wait);
        Assert.assertTrue(mainPage.isWelcomeMessageVisible(), "English main page welcome block should be visible");
    }

    @Test(groups = "web")
    public void searchOpensExactArticle() {
        WikipediaMainPage mainPage = new WikipediaMainPage(driver, wait);
        WikipediaArticlePage articlePage = mainPage.searchFor("Selenium (software)");
        Assert.assertEquals(articlePage.title(), "Selenium (software)", "Article title should match query");
    }

    @Test(groups = "web")
    public void featuredArticleSectionHasTitle() {
        WikipediaMainPage mainPage = new WikipediaMainPage(driver, wait);
        String featuredTitle = mainPage.featuredArticleTitle();
        Assert.assertFalse(featuredTitle.isBlank(), "Featured article title should not be empty");
    }

    @Test(groups = "web")
    public void randomArticleShowsHeadingAndContents() {
        WikipediaMainPage mainPage = new WikipediaMainPage(driver, wait);
        WikipediaArticlePage articlePage = mainPage.openRandomArticle();

        Assert.assertFalse(articlePage.title().isBlank(), "Random article should have a heading");
        Assert.assertTrue(driver.getCurrentUrl().contains("/wiki/"), "Random article URL should contain /wiki/");
    }
}
