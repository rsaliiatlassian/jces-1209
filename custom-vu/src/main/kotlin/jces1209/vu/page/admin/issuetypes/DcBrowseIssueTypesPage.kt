package jces1209.vu.page.admin.issuetypes

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.FalliblePage
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.and
import java.time.Duration

class DcBrowseIssueTypesPage(
    private val jira: WebJira
) : BrowseIssueTypesPage(jira) {

    private val falliblePage = FalliblePage.Builder(
        jira.driver,
        and(
            loadingPageExpectedCondition,
            ExpectedConditions.visibilityOfElementLocated(By.className("aui-page-panel-nav")),
            ExpectedConditions.visibilityOfElementLocated(By.id("issue_types"))
        )
    )
        .serverErrors()
        .timeout(Duration.ofSeconds(30))
        .build()

    override fun waitForBeingLoaded(): DcBrowseIssueTypesPage {
        falliblePage.waitForPageToLoad()
        return this
    }
}
