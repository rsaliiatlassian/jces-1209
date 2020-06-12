package jces1209.vu.page.boards.configure

import jces1209.vu.page.FalliblePage
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class CloudConfigureBoard(
    private val driver: WebDriver
) : ConfigureBoard {

    override fun waitForLoadPage() {
        FalliblePage
            .Builder(
                driver,
                listOf(By.id("ghx-config-nav"), By.id("ghx-config-panel-content"))
            )
            .cloudErrors()
            .build()
            .waitForPageToLoad()
    }
}
