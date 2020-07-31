package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.WebJira

abstract class IssueNavigator(
    private val jira: WebJira
) {
    enum class ViewType {
        DETAIL, LIST
    }

    protected val driver = jira.driver

    abstract fun waitForNavigator()
    abstract fun selectIssue()
    abstract fun changeViewPopup()
    abstract fun getViewType(): ViewType
    abstract fun changeViewType(viewType: ViewType)

    fun openNavigator(): IssueNavigator {
        jira.goToIssueNavigator("resolution = Unresolved ORDER BY priority DESC")
        return this;
    }
}
