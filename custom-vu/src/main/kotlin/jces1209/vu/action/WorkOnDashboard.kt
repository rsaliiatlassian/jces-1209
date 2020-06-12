package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.VIEW_DASHBOARD
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.ProjectMemory
import jces1209.vu.MeasureType.Companion.CREATE_DASHBOARD
import jces1209.vu.MeasureType.Companion.CREATE_GADGET
import jces1209.vu.MeasureType.Companion.LOAD_GADGET
import jces1209.vu.page.dashboard.DashboardPage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class WorkOnDashboard(
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val projectKeyMemory: ProjectMemory,
    private val dashboardPage: DashboardPage

) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)
    override fun run() {
        val projectKey = projectKeyMemory.recall()
        if (projectKey == null) {
            logger.debug("I don't recall any project keys. Maybe next time I will.")
            return
        }
        meter.measure(
            key = VIEW_DASHBOARD,
            action = {
                openDashboardsPage().waitForDashboards()
            }
        )
        createDashboard(dashboardPage)
        createGadget(dashboardPage, projectKey.name)
        loadGadget(dashboardPage)
    }

    private fun openDashboardsPage(): DashboardPage {
        jira.driver.navigate().to("/secure/Dashboard.jspa")
        return dashboardPage
    }

    private fun createDashboard(dashboard: DashboardPage) {
        meter.measure(
            key = CREATE_DASHBOARD,
            action = {
                dashboardPage.createDashboard()
            }
        )
    }

    private fun createGadget(dashboard: DashboardPage, projectKey: String) {
        dashboard.createDashboard()
        meter.measure(
            key = CREATE_GADGET,
            action = {
                dashboard.createGadget(projectKey)
            }
        )
    }

    private fun loadGadget(dashboard: DashboardPage) {
        openDashboardsPage().waitForDashboards()
        meter.measure(
            key = LOAD_GADGET,
            action = {
                dashboard.loadGadget()
            }
        )
    }
}
