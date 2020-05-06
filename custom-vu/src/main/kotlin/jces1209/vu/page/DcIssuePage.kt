package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.page.JiraErrors
import jces1209.vu.wait
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.ExpectedConditions.or
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated

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
}
