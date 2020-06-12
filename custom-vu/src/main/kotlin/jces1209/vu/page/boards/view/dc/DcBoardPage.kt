package jces1209.vu.page.boards.view.dc

import jces1209.vu.page.FalliblePage
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.BoardPage
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration

class DcBoardPage(
    private val driver: WebDriver,
    private val issueSelector: By
) {
    fun waitForBoardPageToLoad(): BoardContent {
        FalliblePage.Builder(
            driver,
            listOf(issueSelector, By.id("ghx-column-headers"))
        )
            .timeout(Duration.ofSeconds(30))
            .serverErrors()
            .build()
            .waitForPageToLoad()
        return BoardPage.GeneralBoardContent(driver, issueSelector)
    }

    fun previewIssue() {
        driver
            .wait(visibilityOfElementLocated(issueSelector))
            .click()

        driver
            .wait(
                ExpectedConditions.and(
                    visibilityOfElementLocated(By.id("ghx-detail-issue")),
                    visibilityOfElementLocated(By.className("issue-drop-zone"))
                ))
    }

    fun closePreviewIssue() {
        val closeButton = driver
            .wait(elementToBeClickable(By.className("aui-iconfont-close-dialog")))
        closeButton.click()

        driver.wait(invisibilityOf(closeButton))
    }
}
