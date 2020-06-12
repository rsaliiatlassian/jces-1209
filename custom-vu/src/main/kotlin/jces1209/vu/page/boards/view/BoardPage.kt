package jces1209.vu.page.boards.view

import jces1209.vu.page.boards.configure.ConfigureBoard
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import java.net.URI

abstract class BoardPage(
    protected val driver: WebDriver,
    protected val uri: URI
) {
    open val issueSelector = By.className("ghx-issue")

    fun goToBoard(): BoardPage {
        driver.navigate().to(uri.toURL())
        return this
    }

    abstract fun getTypeLabel(): String

    abstract fun waitForBoardPageToLoad(): BoardContent

    /**
     * Board must have issues
     */
    abstract fun previewIssue(): BoardPage
    abstract fun closePreviewIssue()

    public class GeneralBoardContent(
        private val driver: WebDriver,
        private val issueSelector: By
    ) : BoardContent {

        private val lazyIssueKeys: Collection<String> by lazy {
            driver
                .findElements(issueSelector)
                .map { it.getAttribute("data-issue-key") }
        }

        override fun getIssueCount(): Int = lazyIssueKeys.size
        override fun getIssueKeys(): Collection<String> = lazyIssueKeys
    }

    protected abstract fun configure(): ConfigureBoard

    fun configureBoard(): ConfigureBoard {
        driver
            .wait(ExpectedConditions.elementToBeClickable(By.id("board-tools-section-button")))
            .click()

        driver
            .wait(ExpectedConditions.elementToBeClickable(By.className("js-view-action-configure")))
            .click()

        return configure()
    }
}
