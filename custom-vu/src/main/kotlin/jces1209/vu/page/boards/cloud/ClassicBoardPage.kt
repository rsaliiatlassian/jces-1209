package jces1209.vu.page.boards.cloud

import jces1209.vu.page.FalliblePage
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.and
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import java.net.URI

internal class ClassicBoardPage(
    private val driver: WebDriver,
    override val uri: URI
) : BoardPage {

    private val issueSelector = By.className("ghx-issue")

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
        return KanbanBoardContent(driver)
    }

    override fun areThereIssues(): Boolean {
        driver
            .wait(visibilityOfElementLocated(By.className("ghx-column")))

        return !driver.findElements(issueSelector).isEmpty()
    }

    override fun issueBentoView(): BoardPage {
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

    private class KanbanBoardContent(
        private val driver: WebDriver
    ) : BoardContent {

        private val lazyIssueKeys: Collection<String> by lazy {
            driver
                .findElements(By.className("ghx-issue"))
                .map { it.getAttribute("data-issue-key") }
        }

        override fun getIssueCount(): Int = lazyIssueKeys.size
        override fun getIssueKeys(): Collection<String> = lazyIssueKeys
    }
}
