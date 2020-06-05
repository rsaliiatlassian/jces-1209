package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.*
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import jces1209.vu.MeasureType.Companion.TOP_BAR_QUICK_SEARCH
import jces1209.vu.page.bars.topBar.TopBar
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Works for both Cloud and Data Center.
 */
class WorkOnTopBar(
    private val topBar: TopBar,
    private val jira: WebJira,
    private val meter: ActionMeter
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun run() {
        jira.navigateTo("secure/BrowseProjects.jspa")
        quickSearch(topBar)
    }

    private fun quickSearch(topBar: TopBar) {
        topBar.waitForTopBar()
        meter.measure(TOP_BAR_QUICK_SEARCH) {
            topBar.quickSearch()
        }
    }
}
