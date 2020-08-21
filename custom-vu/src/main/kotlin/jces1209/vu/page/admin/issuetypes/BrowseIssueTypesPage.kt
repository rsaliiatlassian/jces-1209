package jces1209.vu.page.admin.issuetypes

import com.atlassian.performance.tools.jiraactions.api.WebJira
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions.*

abstract class BrowseIssueTypesPage(
    private val jira: WebJira
) {
    protected val loadingPageExpectedCondition: ExpectedCondition<Boolean> = and(
        visibilityOfElementLocated(By.className("aui-page-panel")),
        visibilityOfElementLocated(By.id("add-issue-type")),
        visibilityOfElementLocated(By.id("issue-types-table")),
        presenceOfAllElementsLocatedBy(By.cssSelector("[data-issue-type-field]")),
        presenceOfAllElementsLocatedBy(By.cssSelector("[data-type]")),
        presenceOfAllElementsLocatedBy(By.className("operations-list")),
        visibilityOfElementLocated(By.className("aui-page-header-inner")),
        presenceOfElementLocated(By.id("admin-nav-heading"))
    )

    fun open(): BrowseIssueTypesPage {
        jira.navigateTo("secure/admin/ViewIssueTypes.jspa")
        return this
    }

    abstract fun waitForBeingLoaded(): BrowseIssueTypesPage
}
