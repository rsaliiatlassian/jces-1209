package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.page.JiraErrors
import jces1209.vu.wait
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
        var assignee = "Tdztbchh Eskfsyas"

        driver
            .findElement(By.cssSelector("[id = 'assignee-val']"))
            .click();

        val userPiccker = driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"assignee-form\"]"))
            )

        Actions(driver)
            .sendKeys(Keys.DELETE)
            .perform()

        Actions(driver)
            .sendKeys(assignee)
            .perform()

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"assignee-group-suggested\"]/option[@data-field-text=\"Tdztbchh Eskfsyas\"]"))
            )

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a/em[.=\"Tdztbchh Eskfsyas\"]"))
            )

        driver
            .findElement(By.xpath("//a/em[.=\"Tdztbchh Eskfsyas\"]"))
            .click();

        Actions(driver).
        sendKeys(Keys.ENTER).
        perform()

        driver.wait(
            ExpectedConditions.presenceOfElementLocated(By.ByXPath("//*[@id = \"issue_summary_assignee_teskfsyas\" and text()[contains(.,'Tdztbchh Eskfsyas')]]"))
        )

        return this;
    }

    override fun transition(): DcIssuePage {
        driver
            .findElement(By.xpath("//*[@id=\"action_id_4\"]//*[. = \"Start Progress\"]"))
            .click();

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"status-val\"]//*[. = \"In Progress\"]"))
            )

        return this;
    }
}
