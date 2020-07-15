package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.bulkOperation.BulkOperation
import jces1209.vu.page.bulkOperation.dc.DcBulkOperation
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration
import java.util.*

class DcIssueNavigator(
    jira: WebJira
) : IssueNavigator(jira) {
    private val meatballTriggerLocator = By.id("AJS_DROPDOWN__80")
    private val bulkEditAllLocator = By.id("bulkedit_max")
    private val falliblePage = FalliblePage.Builder(
        driver,
        and(
            or(
                presenceOfElementLocated(By.xpath("//*[@data-id = 'project']")),
                presenceOfElementLocated(By.cssSelector("ol.issue-list")),
                presenceOfElementLocated(By.id("issuetable")),
                presenceOfElementLocated(By.id("issue-content"))
            ),
            presenceOfElementLocated(By.id("key-val")),
            presenceOfElementLocated(By.className("issue-body-content"))
        )
    )
        .timeout(Duration.ofSeconds(30))
        .serverErrors()
        .build()

    override fun waitForNavigator() {
        falliblePage.waitForPageToLoad()
    }

    override fun selectIssue() {
        val element = getIssueElementFromList()
        val dataKey = element.getAttribute("data-key")
        element.click()
        driver.wait(
            ExpectedConditions.and(
                presenceOfElementLocated(By.xpath("//*[@id = 'key-val' and contains(text(),'$dataKey')]")),
                presenceOfElementLocated(By.id("opsbar-edit-issue_container")),
                visibilityOfElementLocated(By.id("project-avatar"))
            )
        )
    }

    override fun clickOnTools() {
        driver
            .wait(
                ExpectedConditions.elementToBeClickable(meatballTriggerLocator)
            )
            .click()
        driver
            .wait(
                ExpectedConditions.visibilityOfElementLocated(bulkEditAllLocator)
            )
    }

    override fun selectCurrentPageToolsItem(): BulkOperation {
        driver
            .wait(
                ExpectedConditions.visibilityOfElementLocated(bulkEditAllLocator)
            )
            .click()
        val bulkOperation = DcBulkOperation(driver)
        bulkOperation.waitForBulkOperationPage()
        return bulkOperation
    }

    private fun getIssueElementFromList(): WebElement {
        val elements = driver.wait(
            presenceOfAllElementsLocatedBy(By.xpath("//*[@class ='issue-list']/*"))
        )
        val rndIndex = Random().nextInt(elements.size - 1)
        val dataKey = elements[rndIndex].getAttribute("data-key")
        val elementLocator = By.xpath("//*[@data-key = '$dataKey']")
        driver.wait(
            visibilityOfElementLocated(elementLocator)
        )
        driver.wait(
            presenceOfAllElementsLocatedBy(elementLocator)
        )
        return driver.wait(
            elementToBeClickable(elementLocator)
        )
    }
}
