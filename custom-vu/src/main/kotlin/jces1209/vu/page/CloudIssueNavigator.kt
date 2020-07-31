package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration
import java.util.*

class CloudIssueNavigator(
    jira: WebJira
) : IssueNavigator(jira) {
    private val falliblePage = FalliblePage.Builder(
        driver,
        or(
            and(
                or(
                    presenceOfElementLocated(By.cssSelector("ol.issue-list")),
                    presenceOfElementLocated(By.id("issuetable")),
                    presenceOfElementLocated(By.id("issue-content"))
                ),
                or(
                    presenceOfElementLocated(By.id("jira-issue-header")),
                    presenceOfElementLocated(By.id("key-val"))
                ),
                or(
                    presenceOfElementLocated(By.id("new-issue-body-container")),
                    presenceOfElementLocated(By.className("issue-body-content"))
                )
            ),
            //List View
            and(
                presenceOfElementLocated(By.id("issuetable")),
                presenceOfElementLocated(By.id("layout-switcher-toggle"))
            ),
            presenceOfElementLocated(By.className("no-results-hint")) // TODO is it too optimistic like in SearchServerFilter.waitForIssueNavigator ?
        )
    )
        .cloudErrors()
        .timeout(Duration.ofSeconds(30))
        .build()

    override fun waitForNavigator() {
        falliblePage.waitForPageToLoad()
    }

    override fun selectIssue() {
        val element = getIssueElementFromList()
        val title = element.getAttribute("title")
        element.click()
        driver.wait(
            ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-test-id = 'issue.views.issue-base.foundation.summary.heading' and contains(text(), '$title')]")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@aria-label='Not watching']")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-test-id='issue.activity.comment']")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@aria-label='Add attachment']"))
            )
        )
    }

    override fun changeViewPopup(){
        driver.wait(
            ExpectedConditions.elementToBeClickable(By.id("layout-switcher-button"))
        )
            .click()
        driver.wait(
            ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id = 'layout-switcher-button_drop']//*[. = 'Views']")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id = 'layout-switcher-button_drop']//*[. = 'Detail View']")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id = 'layout-switcher-button_drop']//*[. = 'List View']"))
            )
        )
    }

    override fun getViewType(): ViewType {
        val listItems =
            driver.wait(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id = 'layout-switcher-button_drop']//li//span"))
            )

        if (listItems[0].getAttribute("class").contains("aui-iconfont-success")) {
            return IssueNavigator.ViewType.DETAIL
        } else if (listItems[1].getAttribute("class").contains("aui-iconfont-success")) {
            return IssueNavigator.ViewType.LIST
        } else {
            throw Exception("Unrecognized attribute")
        }
    }

    override fun changeViewType(viewType: ViewType) {
        if (viewType == IssueNavigator.ViewType.DETAIL) {
            driver.wait(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id = 'layout-switcher-button_drop']//*[. = 'Detail View']"))
            )
                .click()
        } else if (viewType == IssueNavigator.ViewType.LIST) {
            driver.wait(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id = 'layout-switcher-button_drop']//*[. = 'List View']"))
            )
                .click()
        } else {
            throw Exception("Unrecognized view type")
        }
    }


    private fun getIssueElementFromList(): WebElement {
        val elements = driver.wait(
            ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class ='issue-list']/*[not(@class ='focused')]"))
        )
        val rndIndex = Random().nextInt(elements.size - 1)
        return elements[rndIndex]
    }
}
