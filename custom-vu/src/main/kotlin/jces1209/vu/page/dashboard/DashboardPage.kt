package jces1209.vu.page.dashboard

import com.atlassian.performance.tools.jiraactions.api.page.wait
import jces1209.vu.page.FalliblePage
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import java.time.Duration

abstract class DashboardPage(
    protected val driver: WebDriver
) {
    abstract fun createDashboard()

    fun waitForDashboards() {
        driver.wait(
            condition = ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@href,'/secure/Dashboard.jspa?selectPageId')]")),
            timeout = Duration.ofSeconds(50)
        )
    }

    fun loadGadget() {
        driver.wait(
            condition = ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[contains(@href,'/secure/Dashboard.jspa?selectPageId') " +
                    "and not(@class)])")),
            timeout = Duration.ofSeconds(50)
        ).click()
        FalliblePage.Builder(
            expectedContent = listOf(By.className("dashboard-item-frame gadget-container")),
            webDriver = driver
        )
            .cloudErrors()
            .build()
    }

    fun createGadget(projectName: String) {
        driver.wait(
            condition = ExpectedConditions.elementToBeClickable(By.id("add-gadget")),
            timeout = Duration.ofSeconds(50)
        ).click()
        driver.wait(
            condition = ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@data-item-id=" +
                    "'com.atlassian.jira.gadgets:bubble-chart-dashboard-item']")),
            timeout = Duration.ofSeconds(30)
        ).click()
        driver.wait(
            condition = ExpectedConditions.elementToBeClickable(
                By.className("aui-button aui-button-link button-close-gadgets-dialog")),
            timeout = Duration.ofSeconds(50)
        ).click()
        driver.findElement(By.xpath("(//input[@placeholder='Search'])"))
            .sendKeys(projectName)
        driver.wait(
            condition = ExpectedConditions.visibilityOfElementLocated(
                By.className("aui-list-item-link aui-indented-link")),
            timeout = Duration.ofSeconds(50)
        ).click()
        driver.findElement(By.className("button submit")).click()
        driver.wait(
            condition = ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("bubble-chart-component-plot")),
            timeout = Duration.ofSeconds(50)
        )
    }
}
