package jces1209.vu.page

import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration
import java.util.*


class DcIssueNavigator(
    private val driver: WebDriver
): IssueNavigator {
    private val page = FalliblePage.Builder(
        driver,
        presenceOfElementLocated(By.xpath("//*[@data-id = 'project']"))
    )
        .timeout(Duration.ofSeconds(30))
        .serverErrors()
        .build()

    override fun waitForNavigator() {
        page.waitForPageToLoad()
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
