package jces1209.vu.page.boards.view.cloud

import jces1209.vu.page.boards.configure.CloudConfigureBoard
import jces1209.vu.page.boards.configure.ConfigureBoard
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.ScrumSprintPage
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import java.net.URI

class CloudScrumSprintPage(
    driver: WebDriver, uri: URI
) : ScrumSprintPage(driver, uri) {
    private val cloudClassicBoardPage = CloudClassicBoardPage(
        driver,
        issueSelector,
        listOf(
            By.cssSelector("#ghx-work .ghx-issue:not(.browser-metrics-stale), #ghx-work .ghx-sad-columns:not(.browser-metrics-stale)"),
            By.className("ghx-no-active-sprint-icon")))

    override fun waitForBoardPageToLoad(): BoardContent {
        return cloudClassicBoardPage.waitForBoardPageToLoad()
    }

    override fun previewIssue(): CloudScrumSprintPage {
        cloudClassicBoardPage.previewIssue()
        return this
    }

    override fun closePreviewIssue() {
        cloudClassicBoardPage.closePreviewIssue()
    }

    override fun configure(): ConfigureBoard {
        return CloudConfigureBoard(driver)
    }
}
