package jces1209.vu.page.admin.manageprojects

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.FalliblePage
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration

class DcManageProjectsPage(
    private val jira: WebJira
) : ManageProjectsPage(jira) {

    override val falliblePage = FalliblePage.Builder(
        jira.driver,
        and(
            visibilityOfElementLocated(By.className("aui-page-panel")),
            visibilityOfElementLocated(By.id("view-projects-header")),
            visibilityOfElementLocated(By.id("project-list")),
            visibilityOfElementLocated(By.xpath("//*[@id = 'project-list']//*[. = 'Name']"))
        )
    )
        .serverErrors()
        .timeout(Duration.ofSeconds(30))
        .build()
}
