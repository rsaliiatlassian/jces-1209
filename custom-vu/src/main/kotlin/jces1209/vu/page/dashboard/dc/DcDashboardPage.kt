package jces1209.vu.page.dashboard.dc

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.dashboard.DashboardPage
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions

class DcDashboardPage(
    jira: WebJira
) : DashboardPage(
    driver = jira.driver) {
    override fun createDashboard() {
        driver.findElement(By.className("aui-button aui-dropdown2-trigger " +
            "aui-dropdown2-trigger-arrowless " +
            "active aui-dropdown2-active")).click()
        driver
            .wait(
                ExpectedConditions.visibilityOfElementLocated(By.id("tools-dropdown-items"))
            )
        driver
            .wait(
                ExpectedConditions.elementToBeClickable(By.id("create_dashboard"))
            )
        driver.findElement(
            By.id("edit-entity-dashboard-name"))
            .sendKeys("TestDasboard" + System.currentTimeMillis())
        driver.findElement(
            By.id("edit-entity-submit")).click()
    }
}



