package jces1209.vu.page.boards.view.dc

import com.atlassian.performance.tools.jiraactions.api.WebJira
import org.openqa.selenium.By

class ScrumBoardPage(
    jira: WebJira,
    boardId: String
) : DcBoardPage(
    driver = jira.driver,
    uri = jira.base.resolve("secure/RapidBoard.jspa?rapidView=$boardId&view=planning")
) {
    override val issueSelector = By.className("ghx-issue-compact")
}
