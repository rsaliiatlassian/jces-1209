package jces1209.vu.page

import jces1209.vu.page.mediaviewmodal.DcMediaViewModal
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions.*

class DcAddScreenShot(
    driver: WebDriver
) : AttachScreenShot(driver) {
    override var screenShotLocator = By.xpath("(//*[@class='attachment-thumb])")
    override fun pasteScreenShot() {
        val countBefore = getScreenShotsCount()
        Actions(driver).keyDown(Keys.CONTROL).sendKeys("v").perform()
        driver.wait(
            elementToBeClickable(
                By.className("aui-button aui-button-primary")))
            .click()
        driver.wait(
            and(
                invisibilityOfElementLocated(By.id("attach-screenshot-dialog")),
                numberOfElementsToBeMoreThan(screenShotLocator, countBefore),
                visibilityOfAllElementsLocatedBy(screenShotLocator)
            )
        )
        waitForAttributeValueForElements(By.xpath("//*[contains(@class,'js-file-attachment')]"),
            "data-attachment-thumbnail",
            "true")
    }

    override fun openScreenShot() : DcMediaViewModal {
        val screenShots = getScreenShots()
        val mediaViewModal = DcMediaViewModal(driver)
        if (screenShots.isNotEmpty()) {
            screenShots
                .first()
                .click()
            driver.wait(
                visibilityOfAllElementsLocatedBy(
                    By.xpath("//*[contains(@class,'cp-image-container')]")))
            return mediaViewModal
        } else {
            logger.debug("There are no screenshots to open")
            throw InterruptedException("There are no screenshots to open")
        }
    }
}
