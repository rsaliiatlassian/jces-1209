package jces1209.vu.page

import jces1209.vu.page.bulkOperation.BulkOperation
import com.atlassian.performance.tools.jiraactions.api.WebJira

abstract class IssueNavigator(
    private val jira: WebJira
) {
    protected val driver = jira.driver
    
    abstract fun waitForNavigator()
    abstract fun selectIssue()
    abstract fun clickOnTools()
    abstract fun selectCurrentPageToolsItem(): BulkOperation

    fun openNavigator(): IssueNavigator {
        jira.goToIssueNavigator("resolution = Unresolved ORDER BY priority DESC")
        return this;
    }
}
