package jces1209.vu.page

import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.*

class CloudIssuePage(
    private val driver: WebDriver
) : AbstractIssuePage {
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
            .findElement(By.cssSelector("[data-test-id = 'issue.views.field.rich-text.description']"))
            .click();

        val descriptionForm = driver
            .wait(
                presenceOfElementLocated(By.cssSelector("[data-test-id='issue.views.field.rich-text.editor-container']"))
            )

        Actions(driver)
            .sendKeys(description)
            .perform()

        descriptionForm
            .findElement(By.cssSelector("[data-testid='comment-save-button']"))
            .click()

        driver.wait(
            invisibilityOfAllElements(descriptionForm)
        )
        return this;
    }

    override fun linkIssue(): CloudIssueLinking {
        return CloudIssueLinking(driver)
    }

    override fun changeAssignee(): CloudIssuePage{
        var assignee = "Rostyslav Salii"
        driver
            .findElement(By.cssSelector("[data-test-id = 'issue.views.field.user.assignee']"))
            .click();

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class=\"css-dukhv4-menu fabric-user-picker__menu\"]"))
            )

        Actions(driver)
            .sendKeys(Keys.DELETE)
            .perform()

        Actions(driver)
            .sendKeys(assignee)
            .perform()

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class=\"css-dukhv4-menu fabric-user-picker__menu\"]//*[. = \"Rostyslav Salii\"]"))
            )

        Actions(driver).
            sendKeys(Keys.ENTER).
            perform()

        driver.wait(
            ExpectedConditions.presenceOfElementLocated(By.ByXPath("//*[@data-test-id=\"issue.views.field.user.assignee\"]//*[.=\"Rostyslav Salii\"]"))
        )

        return this;
    }

    override fun transition(): CloudIssuePage {
        driver
            .findElement(By.cssSelector("[data-test-id = 'issue.views.issue-base.foundation.status.status-field-wrapper']"))
            .click();

        var transitionItem = driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-test-id=\"issue.fields.status.common.ui.status-lozenge.4\"]//span[. = \"In Progress\"]"))
            )

        transitionItem.click();

        driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"helpPanelContainer\"]//span[. = \"In Progress\"]"))
            )

        return this;
    }

    private fun isCommentingClassic(): Boolean = driver
        .findElements(By.id("footer-comment-button"))
        .isNotEmpty()
}
