package jces1209.vu

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.action.ProjectSummaryAction
import com.atlassian.performance.tools.jiraactions.api.action.ViewDashboardAction
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.adaptive.AdaptiveIssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.adaptive.AdaptiveJqlMemory
import com.atlassian.performance.tools.jiraactions.api.memories.adaptive.AdaptiveProjectMemory
import jces1209.vu.action.*
import jces1209.vu.memory.BoardPagesMemory
import jces1209.vu.memory.SeededMemory
import jces1209.vu.page.AbstractIssuePage
import jces1209.vu.page.IssueNavigator
import jces1209.vu.page.JiraTips
import jces1209.vu.page.boards.browse.BrowseBoardsPage
import jces1209.vu.page.filters.FiltersPage
import java.net.URI
import java.util.*

class ScenarioSimilarities(
    private val jira: WebJira,
    private val seededRandom: SeededRandom,
    private val meter: ActionMeter
) {

    val jqlMemory = AdaptiveJqlMemory(seededRandom)
        .also { it.remember(listOf("order by created DESC")) } // work around https://ecosystem.atlassian.net/browse/JPERF-573
    val issueKeyMemory = AdaptiveIssueKeyMemory(seededRandom)
    val projectMemory = AdaptiveProjectMemory(seededRandom)
    val filtersMemory = SeededMemory<URI>(seededRandom)
    val boardsMemory = BoardPagesMemory(seededRandom)

    fun assembleScenario(
        issuePage: AbstractIssuePage,
        filtersPage: FiltersPage,
        browseBoardsPage: BrowseBoardsPage,
        createIssue: Action,
        searchWithJql: Action,
        browseProjects: Action,
        customizeColumns: Action,
        issueNavigator: IssueNavigator
    ): List<Action> = assembleScenario(
        createIssue = createIssue,
        customizeColums = customizeColumns,
        searchWithJql = searchWithJql,
        workAnIssue = WorkAnIssue(
            issuePage = issuePage,
            jira = jira,
            meter = meter,
            issueKeyMemory = issueKeyMemory,
            random = seededRandom,
            editProbability = 0.00f, // 0.10f if we can mutate data
            commentProbability = 0.00f, // 0.04f if we can mutate data
            linkIssueProbability = 0.00f, // 0.10f if we can mutate data
            changeAssigneeProbability = 0.00f,
            mentionUserProbability = 0.00f,
            contextOperationProbability = 0.05f
        ),
        projectSummary = ProjectSummaryAction(
            jira = jira,
            meter = meter,
            projectMemory = projectMemory
        ),
        viewDashboard = ViewDashboardAction(
            jira = jira,
            meter = meter
        ),
        browseProjects = browseProjects,
        browseFilters = BrowsePopularFilters(
            filters = filtersMemory,
            filtersPage = filtersPage,
            meter = meter
        ),
        browseBoards = BrowseBoards(
            jira = jira,
            browseBoardsPage = browseBoardsPage,
            meter = meter,
            boardsMemory = boardsMemory
        ),
        viewBoard = ViewBoard(
            driver = jira.driver,
            meter = meter,
            boardsMemory = boardsMemory.all,
            issueKeyMemory = issueKeyMemory,
            random = seededRandom,
            viewIssueProbability = 0.50f,
            configureBoardProbability = 0.05f,
            contextOperationProbability = 0.05f
        ),
        workOnSearchResults = WorkOnSearchResults(
            issueNavigator = issueNavigator,
            jira = jira,
            meter = meter
        ),
        workOnSprint = WorkOnSprint(
            meter = meter,
            scrumBoardsMemory = boardsMemory.scrum,
            jiraTips = JiraTips(jira.driver)
        )
    )

    private fun assembleScenario(
        createIssue: Action,
        customizeColums: Action,
        searchWithJql: Action,
        workAnIssue: Action,
        projectSummary: Action,
        viewDashboard: Action,
        browseProjects: Action,
        browseFilters: Action,
        browseBoards: Action,
        viewBoard: Action,
        workOnSprint: WorkOnSprint,
        workOnSearchResults: Action
    ): List<Action> {
        val exploreData = listOf(browseProjects, browseFilters, browseBoards)
        val spreadOut = mapOf(
            createIssue to 0, // 5 if we can mutate data
            customizeColums to 30,
            searchWithJql to 20,
            workAnIssue to 55,
            projectSummary to 5,
            browseProjects to 5,
            viewDashboard to 0, // 10 when TODO fix the page objects for Cloud
            browseBoards to 5,
            viewBoard to 30,
            workOnSprint to 10,
            workOnSearchResults to 10
        )
            .map { (action, proportion) -> Collections.nCopies(proportion, action) }
            .flatten()
            .shuffled(seededRandom.random)
        return exploreData + spreadOut
    }
}
