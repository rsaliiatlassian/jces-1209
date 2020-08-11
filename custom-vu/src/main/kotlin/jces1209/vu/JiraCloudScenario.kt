package jces1209.vu

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.UserMemory
import com.atlassian.performance.tools.jiraactions.api.scenario.Scenario
import jces1209.vu.action.*
import jces1209.vu.page.CloudIssueNavigator
import jces1209.vu.page.CloudIssuePage
import jces1209.vu.page.admin.ProjectRoles.CloudProjectRolesPage
import jces1209.vu.page.admin.workflow.CloudBrowseWorkflowsPage
import jces1209.vu.page.bars.side.CloudSideBar
import jces1209.vu.page.bars.topBar.dc.DcTopBar
import jces1209.vu.page.boards.browse.cloud.CloudBrowseBoardsPage
import jces1209.vu.page.customizecolumns.CloudColumnsEditor
import jces1209.vu.page.dashboard.cloud.CloudDashboardPage
import jces1209.vu.page.filters.CloudFiltersPage
import jces1209.vu.page.project.CloudProjectNavigatorPage
import org.openqa.selenium.By
import org.openqa.selenium.TakesScreenshot

class JiraCloudScenario : Scenario {
    override fun getLogInAction(
        jira: WebJira,
        meter: ActionMeter,
        userMemory: UserMemory
    ): Action {
        val user = userMemory
            .recall()
            ?: throw Exception("I cannot recall which user I am")
        return LogInWithAtlassianId(user, jira, meter)
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
        return similarities.assembleScenario(
            issuePage = CloudIssuePage(jira.driver),
            filtersPage = CloudFiltersPage(jira, jira.driver),
            browseWorkflowsPage = CloudBrowseWorkflowsPage(jira),
            browseBoardsPage = CloudBrowseBoardsPage(jira),
            dashboardPage = CloudDashboardPage(jira),
            createIssue = CreateAnIssue(
                jira = jira,
                meter = meter,
                projectMemory = similarities.projectMemory,
                createIssueButtons = listOf(By.id("createGlobalItem"), By.id("createGlobalItemIconButton"))
            ),
            browseProjects = BrowseCloudProjects(
                jira = jira,
                meter = meter,
                projectMemory = similarities.projectMemory
            ),
            browseProjectIssues = BrowseProjectIssues(
                jira = jira,
                meter = meter,
                projectKeyMemory = similarities.projectMemory,
                browseProjectPage = CloudProjectNavigatorPage(jira)
            ),
            issueNavigator = CloudIssueNavigator(jira),
            columnsEditor = CloudColumnsEditor(jira.driver),
            topBar = DcTopBar(jira.driver),
            projectRolesPage = CloudProjectRolesPage(jira),
            sideBar = CloudSideBar(jira)
        )
    }
}

