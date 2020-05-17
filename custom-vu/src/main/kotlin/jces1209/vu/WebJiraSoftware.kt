package jces1209.vu

import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jirasoftwareactions.api.page.BrowseBoardsPage
import com.atlassian.performance.tools.jirasoftwareactions.api.page.ViewBacklogPage
import com.atlassian.performance.tools.jirasoftwareactions.api.page.ViewBoardPage
import jces1209.vu.page.DcViewBoard
import org.openqa.selenium.WebDriver

class WebJiraSoftware(
    private val jira: WebJira
) {
    fun goToViewBoard(
        agileBoardId: String
    ): DcViewBoard {
        jira.navigateTo("secure/RapidBoard.jspa?rapidView=$agileBoardId")
        return DcViewBoard(jira.driver)
    }

    fun goToBrowseBoards(): BrowseBoardsPage {
        jira.navigateTo("secure/ManageRapidViews.jspa")
        return BrowseBoardsPage(jira.driver)
    }

    fun goToBacklog(boardId: String): ViewBacklogPage {
        jira.navigateTo("secure/RapidBoard.jspa?rapidView=$boardId&view=planning")
        return ViewBacklogPage(jira.driver)
    }

    fun getDriver(): WebDriver {
        return jira.driver
    }
}
