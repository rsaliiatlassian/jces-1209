package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.page.JiraErrors
import com.atlassian.performance.tools.jiraactions.api.page.wait
import jces1209.vu.wait
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.or
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class DcIssuePage(
    private val driver: WebDriver
) : AbstractIssuePage {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun waitForSummary(): DcIssuePage {
        val jiraErrors = JiraErrors(driver)
        driver.wait(
            or(
                visibilityOfElementLocated(By.id("key-val")),
                jiraErrors.anyCommonError()
            )
        )
        jiraErrors.assertNoErrors()
        return this
    }

    override fun comment(): Commenting {
        return DcCommenting(driver)
    }

    override fun editDescription(description: String): DcIssuePage {
        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("description-val")))
            .click()

        val descriptionForm = driver
            .wait(
                visibilityOfElementLocated(By.id("description-form"))
            )

        Actions(driver)
            .sendKeys(description)
            .perform()

        descriptionForm
            .findElement(By.cssSelector("button[type='submit']"))
            .click()

        driver.wait(
            ExpectedConditions
                .invisibilityOfAllElements(descriptionForm)
        )
        return this
    }

    override fun linkIssue(): DcIssueLinking {
        return DcIssueLinking(driver)
    }

    override fun isTimeSpentFormAppeared(): Boolean {
        try {
            driver
                .wait(
                    ExpectedConditions.presenceOfElementLocated(By.id("log-work-time-logged"))
                )
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override fun cancelTimeSpentForm(): AbstractIssuePage {
        driver
            .wait(
                timeout = Duration.ofSeconds(5),
                condition = ExpectedConditions.presenceOfElementLocated(By.id("issue-workflow-transition-cancel"))
            )
            .click()
        driver
            .wait(
                timeout = Duration.ofSeconds(7),
                condition = ExpectedConditions.invisibilityOfElementLocated(By.id("issue-workflow-transition-cancel"))
            )
        return this
    }

    override fun fillInTimeSpentForm(): AbstractIssuePage {
        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.id("log-work-time-logged"))
            )
            .click()
        Actions(driver)
            .sendKeys("1h")
            .perform()
        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.id("issue-workflow-transition-submit"))
            )
            .click()
        driver
            .wait(
                timeout = Duration.ofSeconds(7),
                condition = ExpectedConditions.invisibilityOfElementLocated(By.id("issue-workflow-transition-cancel"))
            )
        return this
    }

    override fun transition(): DcIssuePage {
        waitForPage()
        WebDriverWait(driver, 7)
            .until {
                try {
                    driver
                        .wait(
                            ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='opsbar-opsbar-transitions']/*"))
                        )
                        .click();
                    true
                } catch (e: Exception) {
                    false
                }
            }
        waitForPage()
        return this
    }

    private fun waitForPage() {
        val executor = driver as JavascriptExecutor
        WebDriverWait(driver, 1)
            .until { executor.executeScript("return document.readyState") == "complete" }
    }
}
