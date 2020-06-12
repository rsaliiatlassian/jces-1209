package jces1209.vu.page.contextoperation

import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions.and
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated

class ContextOperationBoard(
    private val driver: WebDriver
): ContextOperation {
    private val closeButtonLocator = By.id("aui-dialog-close")

    override fun open(): ContextOperationBoard {
        Actions(driver)
            .sendKeys(".")
            .perform()

        driver
            .wait(and(
                visibilityOfElementLocated(By.className("jira-dialog")),
                visibilityOfElementLocated(By.id("issueactions-queryable-container")),
                visibilityOfElementLocated(closeButtonLocator)
            ))
        return this
    }

    override fun close() {
        driver
            .findElement(closeButtonLocator)
            .click()
    }
}
