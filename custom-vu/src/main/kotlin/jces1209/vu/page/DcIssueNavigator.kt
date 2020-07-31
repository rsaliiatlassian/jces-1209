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
                presenceOfElementLocated(By.className("issue-body-content"))
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

    override fun changeViewPopup() {
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
