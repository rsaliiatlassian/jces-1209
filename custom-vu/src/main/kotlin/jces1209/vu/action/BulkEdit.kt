package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.*
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.page.IssueNavigatorPage
import jces1209.vu.MeasureType
import jces1209.vu.MeasureType.Companion.ATTACH_SCREENSHOT
import jces1209.vu.MeasureType.Companion.CONTEXT_OPERATION_ISSUE
import jces1209.vu.MeasureType.Companion.ISSUE_EDIT_DESCRIPTION
import jces1209.vu.MeasureType.Companion.ISSUE_LINK
import jces1209.vu.MeasureType.Companion.ISSUE_LINK_LOAD_FORM
import jces1209.vu.MeasureType.Companion.ISSUE_LINK_SEARCH_CHOOSE
import jces1209.vu.MeasureType.Companion.ISSUE_LINK_SUBMIT
import jces1209.vu.MeasureType.Companion.OPEN_MEDIA_VIEWER
import jces1209.vu.page.AbstractIssuePage
import jces1209.vu.page.AttachScreenShot
import jces1209.vu.page.IssueNavigator
import jces1209.vu.page.bulkOperation.BulkOperation
import jces1209.vu.page.filters.FiltersPage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Works for both Cloud and Data Center.
 */
class BulkEdit(
    private val issueNavigator: IssueNavigator,
    private val bulkOperation: BulkOperation,
    private val jira: WebJira,
    private val meter: ActionMeter
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun run() {
        bulkEdit()
    }

    private fun bulkEdit() {
        val jqlQuery = "order by updated DESC"
        jira.goToIssueNavigator(jqlQuery)
        issueNavigator.waitForNavigator()

        meter.measure(MeasureType.BULK_EDIT) {
            meter.measure(MeasureType.BULK_EDIT_CLICK_ON_TOOLS) {
                issueNavigator.clickOnTools()
            }
            meter.measure(MeasureType.BULK_EDIT_CHOOSE_CURRENT_PAGE) {
                issueNavigator.selectCurrentPageToolsItem()
                bulkOperation.waitForBulkOperationPage()
            }
            meter.measure(MeasureType.BULK_EDIT_CHOOSE_ISSUES) {
                bulkOperation.chooseIssues(100)
                bulkOperation.waitForBulkOperationPage()
            }
            meter.measure(MeasureType.BULK_EDIT_CHOOSE_OPERATION) {
                bulkOperation.chooseOperation()
                bulkOperation.waitForBulkOperationPage()
            }
            meter.measure(MeasureType.BULK_EDIT_OPERATION_DETAILS) {
                bulkOperation.operationDetails()
                bulkOperation.waitForBulkOperationPage()
            }
            meter.measure(MeasureType.BULK_EDIT_CONFIRMATION) {
                bulkOperation.confirmation()
                bulkOperation.waitForBulkOperationPage()
            }
            meter.measure(MeasureType.BULK_EDIT_PROGRESS) {
                bulkOperation.progress()
                bulkOperation.waitForBulkOperationPage()
            }
            meter.measure(MeasureType.BULK_OPERATION_SUBMIT) {
                bulkOperation.submit()
                issueNavigator.waitForNavigator()
            }
        }
    }
}
