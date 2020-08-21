package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.action.Action
import jces1209.vu.Measure
import jces1209.vu.MeasureType
import jces1209.vu.page.admin.issuetypes.BrowseIssueTypesPage

class BrowseIssueTypes(
    private val measure: Measure,
    private val browseIssueTypesPage: BrowseIssueTypesPage
) : Action {

    override fun run() {
        measure.measure(MeasureType.BROWSE_ISSUE_TYPES) {
            browseIssueTypesPage
                .open()
                .waitForBeingLoaded()
        }
    }
}
