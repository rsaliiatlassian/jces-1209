package jces1209.vu.page

import jces1209.vu.wait
import net.bytebuddy.asm.Advice
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import java.security.Key

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
        driver.wait(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"wiki-edit-wikiEdit0\"]")))

        var user = "Tdztbchh Eskfsyas"
        Actions(driver)
            .sendKeys("@")
            .perform()

        Actions(driver)
            .sendKeys(Keys.SHIFT)
            .perform()

        driver.wait(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"mentionDropDown\"]/div")))

        Actions(driver)
            .sendKeys(user)
            .perform()

        driver.wait(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//*[@id=\"mentionDropDown\"]//*[.=\"%s\"]", user))))
        driver.wait(elementToBeClickable(By.xpath(String.format("//*[@id=\"mentionDropDown\"]//*[.=\"%s\"]", user))))
            .click()
    }
}
