package jces1209.vu.page

import jces1209.vu.wait
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.*

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
            .wait(elementToBeClickable(By.cssSelector("[data-test-id = 'issue.views.field.rich-text.description']")))
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

    override fun changeAssignee(): CloudIssuePage {
        driver
            .findElement(By.cssSelector("[data-test-id = 'issue.views.field.user.assignee']"))
            .click();

        var userList = driver
            .wait(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(@class,'fabric-user-picker__menu-list')]/*"))
            )

        if (userList.size == 1 && userList[0].text == "No options") {
            logger.debug("No user options for Assignee")
            throw InterruptedException("No options for Assignee")
        }

        val firstUser = driver
            .wait(
                ExpectedConditions.presenceOfElementLocated(By.id("react-select-assignee-option-0"))
            )

        var userName = firstUser.text.removeSuffix(" (Assign to me)")
        firstUser.click()

        driver.wait(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-test-id='issue.views.field.user.assignee']//*[.='$userName']"))
        )

        return this;
    }

    private fun isCommentingClassic(): Boolean = driver
        .findElements(By.id("footer-comment-button"))
        .isNotEmpty()
}
