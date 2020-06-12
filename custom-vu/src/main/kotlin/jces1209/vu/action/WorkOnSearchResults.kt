package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import jces1209.vu.MeasureType.Companion.SWITCH_BETWEEN_ISSUES_IN_SEARCH_RESULTS
import jces1209.vu.page.CloudIssueNavigator
import jces1209.vu.page.IssueNavigator

class WorkOnSearchResults(
    private val issueNavigator: IssueNavigator,
    private val jira: WebJira,
    private val meter: ActionMeter
) : Action {
    override fun run() {
        jira.goToIssueNavigator("resolution = Unresolved ORDER BY priority DESC")
        issueNavigator.waitForNavigator()

        meter.measure(
            key = SWITCH_BETWEEN_ISSUES_IN_SEARCH_RESULTS,
            action = {
                issueNavigator.selectIssue()
            }
        )
    }
}
