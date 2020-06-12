package jces1209.vu.page.mediaviewmodal

import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions

class CloudMediaViewModal(driver: WebDriver) : MediaViewModal(driver) {
    override fun closeMediaViewModal() {
        driver.wait(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@data-testid='media-viewer-close-button']")))
            .click()
    }
}
