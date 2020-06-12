package jces1209.vu.page

import jces1209.vu.page.mediaviewmodal.MediaViewModal
import jces1209.vu.wait
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.*
import org.openqa.selenium.support.ui.ExpectedConditions

abstract class AttachScreenShot(
    protected val driver: WebDriver
) {
    protected val logger: Logger = LogManager.getLogger(this::class.java)
    abstract val screenShotLocator: By

    fun makeScreenShot() {
        (driver as TakesScreenshot).getScreenshotAs(OutputType.BYTES)
    }

    abstract fun pasteScreenShot()

    protected fun waitForAttributeValueForElements(by: By, attributeName: String, attributeValue: String) {
        driver
            .findElements(by)
            .forEach {
                driver.wait(ExpectedConditions.attributeContains(it, attributeName, attributeValue))
            }
    }

    protected fun getScreenShotsCount(): Int {
        return driver.findElements(screenShotLocator).size
    }

    abstract fun openScreenShot(): MediaViewModal
    protected fun getScreenShots(): List<WebElement> {
        return driver.findElements(screenShotLocator)
    }

}
