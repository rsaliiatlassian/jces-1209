package jces1209.vu.page.admin.manageprojects

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.FalliblePage
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration

class CloudManageProjectsPage(
    private val jira: WebJira
) : ManageProjectsPage(jira) {

    private val tableContainerLocator = By.xpath("//*[@data-test-id = 'global-pages.directories.directory-base.content.table.container']")!!
    override val falliblePage = FalliblePage.Builder(
        jira.driver,
        and(
            visibilityOfElementLocated(By.className("aui-page-panel")),
            visibilityOfElementLocated(tableContainerLocator),
            visibilityOfNestedElementsLocatedBy(tableContainerLocator, By.xpath("//*[. = 'Name']")),
            visibilityOfNestedElementsLocatedBy(tableContainerLocator, By.xpath("//*[@role = 'presentation']"))
        )
    )
        .cloudErrors()
        .timeout(Duration.ofSeconds(30))
        .build()
}
