package jces1209.vu.page.contextoperation

import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions.and
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated

class ContextOperationIssue(
    private val driver: WebDriver
) : ContextOperation {

    override fun open(): ContextOperationIssue {
        Actions(driver)
            .sendKeys(".")
            .perform()

        driver
            .wait(and(
                visibilityOfElementLocated(By.id("shifter-dialog")),
                visibilityOfElementLocated(By.id("shifter-dialog-field"))
            ))
        return this
    }

    override fun close() {
        Actions(driver)
            .sendKeys(Keys.ESCAPE)
            .perform()
    }
}
