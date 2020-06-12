package jces1209.vu.page.boards.view.cloud

import jces1209.vu.page.boards.configure.CloudConfigureBoard
import jces1209.vu.page.boards.configure.ConfigureBoard
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.KanbanBoardPage
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import java.net.URI

class CloudKanbanBoardPage(
    driver: WebDriver, uri: URI
) : KanbanBoardPage(driver, uri) {
    private val cloudClassicBoardPage = CloudClassicBoardPage(
        driver,
        issueSelector,
        listOf(By.cssSelector("#ghx-work .ghx-issue:not(.browser-metrics-stale), #ghx-work .ghx-sad-columns:not(.browser-metrics-stale)")))

    override fun waitForBoardPageToLoad(): BoardContent {
        return cloudClassicBoardPage.waitForBoardPageToLoad()
    }

    override fun previewIssue(): CloudKanbanBoardPage {
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
