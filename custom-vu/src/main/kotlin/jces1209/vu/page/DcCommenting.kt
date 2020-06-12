package jces1209.vu.page

import jces1209.vu.page.Commenting
import jces1209.vu.page.InlineCommentForm
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated

class DcCommenting(
    private val driver: WebDriver
) : Commenting {

    override fun openEditor() {
        driver
            .wait(elementToBeClickable(By.id("footer-comment-button")))
            .click()
        InlineCommentForm(driver).waitForButton()
    }

    override fun typeIn(comment: String) {
        InlineCommentForm(driver).enterCommentText(comment)
    }

    override fun saveComment() {
        InlineCommentForm(driver).submit()
    }

    override fun waitForTheNewComment() {
        driver.wait(visibilityOfElementLocated(By.cssSelector(".activity-comment.focused")))
    }

    override fun mentionUser() {
        driver.wait(ExpectedConditions.presenceOfElementLocated(By.id("wiki-edit-wikiEdit0")))
        Actions(driver)
            .sendKeys("@", Keys.ARROW_RIGHT)
            .perform()
        var mentionUser = driver.wait(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='mentionDropDown']/div//li/a")))
            .text
            .substringBefore(" - ")
        Actions(driver)
            .sendKeys(mentionUser)
            .perform()
        driver.wait(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='mentionDropDown']//*[.='$mentionUser']")))
        driver.wait(elementToBeClickable(By.xpath("//*[@id='mentionDropDown']//*[.='$mentionUser']")))
            .click()
    }
}
