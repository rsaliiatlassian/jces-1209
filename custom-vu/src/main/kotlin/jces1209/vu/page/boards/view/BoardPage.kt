package jces1209.vu.page.boards.view

import com.atlassian.performance.tools.jiraactions.api.WebJira
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
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

    abstract fun waitForBoardPageToLoad(): BoardContent

    /**
     * Board must have issues
     */
    abstract fun previewIssue(): BoardPage

    protected class GeneralBoardContent(
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
}
