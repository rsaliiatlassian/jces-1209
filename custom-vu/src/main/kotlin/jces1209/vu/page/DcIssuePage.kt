package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.page.JiraErrors
import com.atlassian.performance.tools.jiraactions.api.page.wait
import jces1209.vu.page.contextoperation.ContextOperationIssue
import jces1209.vu.wait
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
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
    private val transitionBtn = By.id("opsbar-opsbar-transitions")
    private val falliblePage = FalliblePage.Builder(
        expectedContent = listOf(transitionBtn),
        webDriver = driver
    )
        .cloudErrors()
        .build()

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

    override fun addAttachment(): DcAddScreenShot {
        return DcAddScreenShot(driver)
    }

    override fun linkIssue(): DcIssueLinking {
        return DcIssueLinking(driver)
    }

    override fun changeAssignee(): DcIssuePage {
        driver
            .findElement(By.id("assignee-val"))
            .click();

        val userPicker = driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.id("assignee-form"))
            )

        Actions(driver)
            .sendKeys(Keys.DELETE, Keys.ARROW_DOWN)
            .perform()

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.id("assignee-group-suggested"))
            )

        val allUsers = driver
            .wait(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='all-users']//*[@role='option']"))
            )

        var user = if (allUsers.count() > 2) {
            allUsers[2].text.substringBefore(" - ")
        } else {
            if (allUsers.count() >= 0) {
                allUsers[0].text.substringBefore(" - ")
            } else {
                logger.debug("No user options for Assignee")
                throw InterruptedException("No options for Assignee")
            }
        }

        Actions(driver)
            .sendKeys(user)
            .perform()

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id ='assignee-single-select' and @data-query = '$user']"))
            )
            .click()

        Actions(driver)
            .sendKeys(Keys.ENTER)
            .perform()

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"assignee-form\"]//button[@type='submit']"))
            )
            .click()

        driver.wait(
            ExpectedConditions.presenceOfElementLocated(By.ByXPath(String.format("//*[@id=\"assignee-val\" and @title = \"Click to edit\"]")))
        )

        return this;
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
        val cancelButton = org.openqa.selenium.By.id("issue-workflow-transition-cancel")
        driver
            .wait(
                timeout = Duration.ofSeconds(5),
                condition = ExpectedConditions.presenceOfElementLocated(cancelButton)
            )
            .click()
        driver
            .wait(
                timeout = Duration.ofSeconds(7),
                condition = ExpectedConditions.invisibilityOfElementLocated(cancelButton)
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
        falliblePage.waitForPageToLoad()
        //TODO("reduce this workaround")
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
        return this
    }

    override fun contextOperation(): ContextOperationIssue {
        return ContextOperationIssue(driver)
    }
}
