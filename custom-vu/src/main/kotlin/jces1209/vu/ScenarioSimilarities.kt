package jces1209.vu

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.action.ProjectSummaryAction
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
import jces1209.vu.page.bars.side.SideBar
import jces1209.vu.page.bars.topBar.TopBar
import jces1209.vu.page.boards.browse.BrowseBoardsPage
import jces1209.vu.page.bulkOperation.BulkOperation
import jces1209.vu.page.customizecolumns.ColumnsEditor
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
        browseProjects: Action,
        workOnDashboard: Action,
        browseProjectIssues: Action,
        issueNavigator: IssueNavigator,
        columnsEditor: ColumnsEditor,
        topBar: TopBar,
        bulkOperation: BulkOperation,
        sideBar: SideBar
    ): List<Action> = assembleScenario(
        createIssue = createIssue,
        workOnDashboard = workOnDashboard,
        workAnIssue = WorkOnIssue(
            issuePage = issuePage,
            jira = jira,
            meter = meter,
            issueKeyMemory = issueKeyMemory,
            random = seededRandom,
            editProbability = 0.00f, // 0.10f if we can mutate data
            commentProbability = 0.00f, // 0.04f if we can mutate data
            linkIssueProbability = 0.00f, // 0.10f if we can mutate data
            attachScreenShotProbability = 0.05f,
            changeAssigneeProbability = 0.00f,
            mentionUserProbability = 0.00f,
            transitionProbability = 0.00f,
            contextOperationProbability = 0.05f
        ),
        projectSummary = ProjectSummaryAction(
            jira = jira,
            meter = meter,
            projectMemory = projectMemory
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
        workOnSprint = WorkOnSprint(
            meter = meter,
            backlogsMemory = boardsMemory.backlog,
            sprintsMemory = boardsMemory.sprint,
            jiraTips = JiraTips(jira.driver)
        ),
        browseProjectIssues = browseProjectIssues,
        workOnTopBar = WorkOnTopBar(
            topBar = topBar,
            jira = jira,
            meter = meter
        ),
        workOnSearch = WorkOnSearch(
            issueNavigator = issueNavigator,
            jira = jira,
            meter = meter,
            columnsEditor = columnsEditor,
            random = seededRandom,
            filters = filtersMemory,
            jqlMemory = jqlMemory,
            issueKeyMemory = issueKeyMemory,
            searchFilterProbability = 0.50f,
            searchJclProbability = 0.05f,
            globalSearchProbability = 0.05f,
            customizeColumnsProbability = 0.05f,
            switchBetweenIssuesProbability = 0.05f
        ),
        bulkEdit = BulkEdit(
            issueNavigator = issueNavigator,
            bulkOperation = bulkOperation,
            jira = jira,
            meter = meter
        ),
        workOnTransition = WorkOnTransition(
            meter = meter,
            boardsMemory = boardsMemory.sprint,
            sideBar = sideBar,
            issueNavigator = issueNavigator
        )
    )

    private fun assembleScenario(
        createIssue: Action,
        workAnIssue: Action,
        projectSummary: Action,
        browseProjects: Action,
        browseFilters: Action,
        browseBoards: Action,
        viewBoard: Action,
        workOnDashboard: Action,
        workOnSprint: WorkOnSprint,
        browseProjectIssues: Action,
        workOnSearch: Action,
        workOnTopBar: Action,
        bulkEdit: Action,
        workOnTransition: Action
    ): List<Action> {
        val exploreData = listOf(browseProjects, browseFilters, browseBoards)
        val spreadOut = mapOf(
            createIssue to 0, // 5 if we can mutate data
            workAnIssue to 55,
            projectSummary to 5,
            browseProjects to 5,
            browseBoards to 5,
            viewBoard to 30,
            workOnDashboard to 5,
            workOnSprint to 10,
            browseProjectIssues to 5,
            workOnSearch to 5,
            workOnTopBar to 5,
            bulkEdit to 5,
            workOnTransition to 5
        )
            .map { (action, proportion) -> Collections.nCopies(proportion, action) }
            .flatten()
            .shuffled(seededRandom.random)
        return exploreData + spreadOut
    }
}
