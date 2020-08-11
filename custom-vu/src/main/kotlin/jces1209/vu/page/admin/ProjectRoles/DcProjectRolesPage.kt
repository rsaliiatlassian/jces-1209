package jces1209.vu.page.admin.ProjectRoles

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.FalliblePage
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration

class DcProjectRolesPage(
    private val jira: WebJira
) : ProjectRolesPage(jira) {

    private val falliblePage = FalliblePage.Builder(
        jira.driver,
        and(
            visibilityOfElementLocated(By.className("aui-page-panel")),
            visibilityOfElementLocated(By.ById("project_role_browser")),
            visibilityOfElementLocated(By.xpath("//*[@id = 'content']//*[. = 'System']")),
            visibilityOfElementLocated(By.xpath("//*[@id = 'content']//*[. = 'Project Role Browser']")),
            presenceOfElementLocated(By.id("project-role-Administrators"))
        )
    )
        .serverErrors()
        .timeout(Duration.ofSeconds(30))
        .build()

    override fun waitForBeingLoaded(): DcProjectRolesPage {
        falliblePage.waitForPageToLoad()
        return this
    }
}
