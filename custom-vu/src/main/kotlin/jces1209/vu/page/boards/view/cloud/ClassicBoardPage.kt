package jces1209.vu.page.boards.view.cloud

import jces1209.vu.page.FalliblePage
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.BoardPage
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.and
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import java.net.URI

abstract class ClassicBoardPage(
    driver: WebDriver, uri: URI
) : BoardPage(driver, uri) {

    private val falliblePage = FalliblePage.Builder(
        webDriver = driver,
        expectedContent = listOf(
            By.xpath("//*[contains(text(), 'Your board has too many issues')]"),
            By.xpath("//*[contains(text(), 'Board not accessible')]"),
            By.xpath("//*[contains(text(), 'Set a new location for your board')]"),
            By.cssSelector(".ghx-column")
        )
    )
        .cloudErrors()
        .build()

    override fun waitForBoardPageToLoad(): BoardContent {
        falliblePage.waitForPageToLoad()
        return GeneralBoardContent(driver, issueSelector)
    }

    override fun previewIssue(): ClassicBoardPage {
        driver
            .wait(visibilityOfElementLocated(issueSelector))
            .click()

        driver
            .wait(
                and(
                    visibilityOfElementLocated(By.cssSelector("[role='dialog']")),
                    visibilityOfElementLocated(By.cssSelector("[data-test-id='issue-activity-feed.heading']"))
                ))

        return this;
    }
}
