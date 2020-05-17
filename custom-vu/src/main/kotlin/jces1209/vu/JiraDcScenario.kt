package jces1209.vu

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.action.BrowseProjectsAction
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.UserMemory
import com.atlassian.performance.tools.jiraactions.api.scenario.JiraCoreScenario
import com.atlassian.performance.tools.jiraactions.api.scenario.Scenario
import com.atlassian.performance.tools.jirasoftwareactions.api.actions.BrowseBoardsAction
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AdaptiveBoardMemory
import jces1209.vu.action.BrowseDcBoards
import jces1209.vu.action.CreateAnIssue
import jces1209.vu.action.SearchServerFilter
import jces1209.vu.action.ViewDcBoard
import jces1209.vu.page.DcIssuePage
import jces1209.vu.page.filters.ServerFiltersPage
import org.openqa.selenium.By
import org.openqa.selenium.TakesScreenshot

class JiraDcScenario : Scenario {

    override fun getLogInAction(
        jira: WebJira,
        meter: ActionMeter,
        userMemory: UserMemory
    ): Action {
        return JiraCoreScenario().getLogInAction(jira, meter, userMemory)
    }

    override fun getActions(
        jira: WebJira,
        seededRandom: SeededRandom,
        actionMeter: ActionMeter
    ): List<Action> {
        val meter = ActionMeter.Builder(actionMeter)
            .appendPostMetricHook(
                TakeScreenshotHook.Builder(
                    jira.driver as TakesScreenshot
                ).build())
            .build()
        val similarities = ScenarioSimilarities(jira, seededRandom, meter)
        val jsw = WebJiraSoftware(jira)
        val boardsMemory = AdaptiveBoardMemory<AgileBoard>(seededRandom)
        return similarities.assembleScenario(
            issuePage = DcIssuePage(jira.driver),
            filtersPage = ServerFiltersPage(jira, jira.driver),
            createIssue = CreateAnIssue(
                jira = jira,
                meter = meter,
                projectMemory = similarities.projectMemory,
                createIssueButton = By.id("create_link")
            ),
            searchWithJql = SearchServerFilter(
                jira = jira,
                meter = meter,
                filters = similarities.filtersMemory
            ),
            browseProjects = BrowseProjectsAction(
                jira = jira,
                meter = meter,
                projectMemory = similarities.projectMemory
            ),
            browseBoards = BrowseDcBoards(
                jiraSoftware = jsw,
                meter = meter,
                boardsMemory = boardsMemory,
                scrumBoardsMemory = AdaptiveBoardMemory(seededRandom)
            ),
            viewBoard = ViewDcBoard(
                jiraSoftware = jsw,
                meter = meter,
                boardMemory = boardsMemory,
                issueKeyMemory = similarities.issueKeyMemory,
                random = seededRandom,
                viewIssueProbability = 0.10f
            )
        )
    }
}
