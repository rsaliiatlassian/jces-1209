package jces1209.vu.page.boards.view

import org.openqa.selenium.WebDriver
import java.net.URI

abstract class NextGenBoardPage(
    driver: WebDriver,
    uri: URI
) : BoardPage(
    driver = driver,
    uri = uri
) {
    override fun getTypeLabel(): String {
        return "Next-gen"
    }
}
