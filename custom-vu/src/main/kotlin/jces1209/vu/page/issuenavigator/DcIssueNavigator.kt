package jces1209.vu.page.issuenavigator

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.FalliblePage
import jces1209.vu.page.ViewSubscriptions.dc.DcViewSubscriptions
import jces1209.vu.page.issuenavigator.bulkoperation.BulkOperationPage
import jces1209.vu.page.issuenavigator.bulkoperation.DcBulkOperationPage
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration
import java.util.*

class DcIssueNavigator(
    jira: WebJira
) : IssueNavigator(jira) {
    private val falliblePage = FalliblePage.Builder(
        driver,
        or(
            and(
                or(
                    presenceOfElementLocated(By.xpath("//*[@data-id = 'project']")),
                    presenceOfElementLocated(By.cssSelector("ol.issue-list")),
                    presenceOfElementLocated(By.id("issuetable")),
                    presenceOfElementLocated(By.id("issue-content"))
                ),
                presenceOfElementLocated(By.id("key-val")),
                presenceOfElementLocated(By.className("issue-body-content")),
                presenceOfElementLocated(filterDetailsLocator)
            ),
            and(
                presenceOfElementLocated(By.id("issuetable")),
                presenceOfElementLocated(By.id("layout-switcher-toggle"))
            )
        )
    )
        .timeout(Duration.ofSeconds(30))
        .serverErrors()
        .build()

    override val filterSubscriptionFalliblePage = FalliblePage.Builder(
        driver,
        filterSubscriptionList
    )
        .cloudErrors()
        .timeout(Duration.ofSeconds(30))
        .build()

    override val viewSubscriptions = DcViewSubscriptions(jira)

    override fun waitForBeingLoaded(): DcIssueNavigator {
        falliblePage.waitForPageToLoad()
        return this
    }

    override fun selectIssue() {
        val element = getIssueElementFromList()
        val dataKey = element.getAttribute("data-key")
        element.click()
        driver.wait(
            and(
                presenceOfElementLocated(By.xpath("//*[@id = 'key-val' and contains(text(),'$dataKey')]")),
                presenceOfElementLocated(By.id("opsbar-edit-issue_container")),
                visibilityOfElementLocated(By.id("project-avatar"))
            )
        )
    }

    override fun clickDetails() {
        driver.wait(
            invisibilityOfElementLocated(By.id("aui-flag-container"))
        )
        super.clickDetails()
    }

    override fun openBulkOperation(): BulkOperationPage {
        driver
            .wait(elementToBeClickable(By.id("AJS_DROPDOWN__80")))
            .click()
        driver
            .wait(visibilityOfElementLocated(By.cssSelector("#bulkedit_max, #bulkedit_all")))
            .click()
        return DcBulkOperationPage(jira)
            .waitForBeingLoaded()
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
