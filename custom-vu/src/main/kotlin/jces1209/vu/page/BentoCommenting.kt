package jces1209.vu.page

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable
import jces1209.vu.wait
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated

class BentoCommenting(
    private val driver: WebDriver
) : Commenting {

    override fun openEditor() {
        clickThePlaceholder()
        waitForEditor()
    }

    private fun clickThePlaceholder() {
        waitForPlaceholder().click()
    }

    private fun waitForPlaceholder(): WebElement = driver.wait(
        elementToBeClickable(By.cssSelector("[placeholder='Add a comment…']"))
    )

    private fun waitForEditor() = findSaveButton()

    private fun findSaveButton() = driver.wait(elementToBeClickable(By.xpath("//*[contains(text(),'Save')]")))

    override fun typeIn(comment: String) {
        driver.wait(elementToBeClickable(By.xpath("//*[contains(text(),'Add a comment…')]")))
        Actions(driver)
            .sendKeys(comment)
            .perform()
    }

    override fun saveComment() {
        findSaveButton().click()
    }

    override fun waitForTheNewComment() {
        waitForPlaceholder()
    }

    override fun mentionUser() {
        Actions(driver)
            .sendKeys("@")
            .perform()
        driver.wait(presenceOfElementLocated(By.xpath("//*[@data-editor-popup='true']//*[.='Mention your team']")))

        var userElement =
            driver.wait(presenceOfElementLocated(By.xpath("//*[@data-editor-popup='true']//*[@data-mention-id]")))

        var user = userElement.text

        Actions(driver)
            .sendKeys(user)
            .perform()
        driver.wait(elementToBeClickable(By.xpath("//*[contains(@data-mention-name,'$user]")))
            .click()
    }
}
