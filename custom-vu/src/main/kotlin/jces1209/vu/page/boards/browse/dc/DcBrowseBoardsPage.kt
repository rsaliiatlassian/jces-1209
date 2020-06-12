package jces1209.vu.page.boards.browse.dc

import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.page.JiraErrors
import com.atlassian.performance.tools.jiraactions.api.page.wait
import jces1209.vu.page.boards.browse.BrowseBoardsPage
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import java.time.Duration

class DcBrowseBoardsPage(
    private val jira: WebJira
) : BrowseBoardsPage {

    override fun goToBoardsPage(): DcBrowseBoardsPage {
        jira.navigateTo("secure/ManageRapidViews.jspa")
        return this;
    }

    override fun waitForBoards(): DcBoardList {
        val jiraErrors = JiraErrors(jira.driver)
        jira.driver.wait(
            Duration.ofSeconds(30),
            ExpectedConditions.or(
                ExpectedConditions.and(
                    ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#ghx-header h2"), "Boards"),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("#ghx-content-main table.aui"))
                ),
                jiraErrors.anyCommonError()
            )
        )
        jiraErrors.assertNoErrors()

        return DcBoardList(jira)
    }
}
