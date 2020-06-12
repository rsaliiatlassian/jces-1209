package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.page.wait
import jces1209.vu.wait
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class CloudIssuePage(
    private val driver: WebDriver
) : AbstractIssuePage {
    private val logger: Logger = LogManager.getLogger(this::class.java)
    private val bentoSummary = By.cssSelector("[data-test-id='issue.views.issue-base.foundation.summary.heading']")
    private val classicSummary = By.id("key-val")
    private val falliblePage = FalliblePage.Builder(
        expectedContent = listOf(bentoSummary, classicSummary),
        webDriver = driver
    )
        .cloudErrors()
        .build()

    override fun waitForSummary(): AbstractIssuePage {
        falliblePage.waitForPageToLoad()
        return this
    }

    override fun comment(): Commenting {
        return if (isCommentingClassic()) {
            ClassicCloudCommenting(driver)
        } else {
            BentoCommenting(driver)
        }
    }

    override fun editDescription(description: String): CloudIssuePage {
        driver
            .wait(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test-id = 'issue.views.field.rich-text.description']")))
            .click();

        val descriptionForm = driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test-id='issue.views.field.rich-text.editor-container']"))
            )

        Actions(driver)
            .sendKeys(description)
            .perform()

        descriptionForm
            .findElement(By.cssSelector("[data-testid='comment-save-button']"))
            .click()

        driver.wait(
            ExpectedConditions.invisibilityOfAllElements(descriptionForm)
        )
        return this;
    }

    override fun linkIssue(): CloudIssueLinking {
        return CloudIssueLinking(driver)
    }

    override fun isTimeSpentFormAppeared(): Boolean {
        try {
            driver
                .wait(
                    ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(By.id("log-work-time-logged"))
                    )
                )
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override fun cancelTimeSpentForm(): AbstractIssuePage {
        val transitionCancelLocator = By.id("issue-workflow-transition-cancel")
        driver
            .wait(
                timeout = Duration.ofSeconds(5),
                condition = ExpectedConditions.presenceOfElementLocated(transitionCancelLocator)
            )
            .click()
        driver
            .wait(
                timeout = Duration.ofSeconds(7),
                condition = ExpectedConditions.invisibilityOfElementLocated(transitionCancelLocator)
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
                ExpectedConditions.presenceOfElementLocated(By.id("comment"))
            )
            .click()
        Actions(driver)
            .sendKeys("comment")
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
        driver
            .wait(
                timeout = Duration.ofSeconds(3),
                condition = ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"helpPanelContainer\"]"))
            )
        return this
    }

    private fun waitForNotWatchingElement(): WebElement = driver.wait(
        timeout = Duration.ofSeconds(7),
        condition = ExpectedConditions.elementToBeClickable(By.xpath("//*[@aria-label = 'Not watching']"))
    )

    private fun waitForTransitionButton(): WebElement = driver.wait(
        timeout = Duration.ofSeconds(7),
        condition = ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-test-id = 'issue.views.issue-base.foundation.status.status-field-wrapper']//button"))
    )

    private fun clickOnElementUntilVisible(element: By, conditionOfSuccess: By) {
        WebDriverWait(driver, 7)
            .until {
                try {
                    driver.wait(ExpectedConditions.elementToBeClickable(element))
                        .click()
                    driver.wait(ExpectedConditions.presenceOfElementLocated(conditionOfSuccess))
                    true
                } catch (e: Exception) {
                    false
                }
            }
    }

    override fun transition(): CloudIssuePage {
        falliblePage.waitForPageToLoad()
        waitForNotWatchingElement()
        waitForTransitionButton()
        clickOnElementUntilVisible(By.xpath("//*[@data-test-id = 'issue.views.issue-base.foundation.status.status-field-wrapper']//button"),
            By.xpath("//*[@data-test-id = 'issue.views.issue-base.foundation.status.status-field-wrapper']//*[contains(@data-test-id,\"issue.fields.status.common.ui.status-lozenge\")]")
        )
        driver
            .wait(
                timeout = Duration.ofSeconds(7),
                condition = ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-test-id = 'issue.views.issue-base.foundation.status.status-field-wrapper']//*[contains(@data-test-id,\"issue.fields.status.common.ui.status-lozenge\")]"))
            )
            .click()
        return this
    }

    private fun isCommentingClassic(): Boolean = driver
        .findElements(By.id("footer-comment-button"))
        .isNotEmpty()
}
