package jces1209.vu.page.mediaviewmodal

import org.openqa.selenium.WebDriver

abstract class MediaViewModal(
    protected val driver: WebDriver
) {
    abstract fun closeMediaViewModal()
}
