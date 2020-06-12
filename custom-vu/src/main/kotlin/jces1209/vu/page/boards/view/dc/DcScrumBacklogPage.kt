package jces1209.vu.page.boards.view.dc

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.boards.configure.ConfigureBoard
import jces1209.vu.page.boards.configure.DcConfigureBoard
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.ScrumBacklogPage
import org.openqa.selenium.By

class DcScrumBacklogPage(
    jira: WebJira,
    boardId: String
) : ScrumBacklogPage(
    driver = jira.driver,
    uri = jira.base.resolve("secure/RapidBoard.jspa?rapidView=$boardId&view=planning&issueLimit=100")
) {
    override val issueSelector = By.className("js-issue")
    private val dcBoardPage = DcBoardPage(driver, issueSelector)

    override fun waitForBoardPageToLoad(): BoardContent {
        return dcBoardPage.waitForBoardPageToLoad()
    }

    override fun previewIssue(): DcScrumBacklogPage {
        dcBoardPage.previewIssue()
        return this
    }

    override fun closePreviewIssue() {
        dcBoardPage.closePreviewIssue()
    }

    override fun configure(): ConfigureBoard {
        return DcConfigureBoard(driver)
    }
}
