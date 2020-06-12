package jces1209.vu.page.boards.view.cloud

import jces1209.vu.page.boards.configure.CloudConfigureBoard
import jces1209.vu.page.boards.configure.ConfigureBoard
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.ScrumBacklogPage
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import java.net.URI

class CloudScrumBacklogPage(
    driver: WebDriver, uri: URI
) : ScrumBacklogPage(
    driver,
    uri) {
    override val issueSelector = By.className("js-issue")
    private val cloudClassicBoardPage = CloudClassicBoardPage(
        driver,
        issueSelector,
        listOf(
            By.cssSelector(".js-marker-backlog-header:not(.browser-metrics-stale)"),
            By.cssSelector(".ghx-backlog-container > .ghx-no-issues.js-empty-list")))

    override fun waitForBoardPageToLoad(): BoardContent {
        return cloudClassicBoardPage.waitForBoardPageToLoad()
    }

    override fun previewIssue(): CloudScrumBacklogPage {
        driver
            .wait(visibilityOfElementLocated(issueSelector))
            .click()

        driver
            .wait(
                ExpectedConditions.and(
                    visibilityOfElementLocated(By.cssSelector("[data-test-id='issue.views.issue-base.context.context-items.primary-items']")),
                    visibilityOfElementLocated(By.cssSelector("[data-test-id='issue-activity-feed.heading']"))
                ))
        return this
    }

    override fun closePreviewIssue() {
        cloudClassicBoardPage.closePreviewIssue()
    }

    override fun configure(): ConfigureBoard {
        return CloudConfigureBoard(driver)
    }
}
