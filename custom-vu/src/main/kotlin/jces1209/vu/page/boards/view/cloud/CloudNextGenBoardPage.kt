package jces1209.vu.page.boards.view.cloud

import jces1209.vu.page.FalliblePage
import jces1209.vu.page.boards.configure.CloudConfigureBoard
import jces1209.vu.page.boards.configure.ConfigureBoard
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.NextGenBoardPage
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import java.net.URI

class CloudNextGenBoardPage(
    driver: WebDriver, uri: URI
) : NextGenBoardPage(driver, uri) {
    override val issueSelector = By.cssSelector("[data-test-id='platform-board-kit.ui.card.card']")

    private val falliblePage = FalliblePage.Builder(
        webDriver = driver,
        expectedContent = listOf(
            By.xpath("//*[contains(text(), 'Your board has too many issues')]"),
            By.xpath("//*[contains(text(), 'Board not accessible')]"),
            By.cssSelector("[data-test-id='platform-board-kit.common.ui.column-header.header.column-header-container']")
        )
    )
        .cloudErrors()
        .build()

    override fun waitForBoardPageToLoad(): BoardContent {
        driver.get(uri.toString())
        closeModals()

        falliblePage.waitForPageToLoad()

        val issueCards = findIssueCards()
        return NextGenBoardContent(issueCards)
    }

    override fun previewIssue(): CloudNextGenBoardPage {
        driver
            .wait(visibilityOfElementLocated(issueSelector))
            .click()

        driver
            .wait(
                ExpectedConditions.and(
                    visibilityOfElementLocated(By.cssSelector("[role='dialog']")),
                    visibilityOfElementLocated(By.cssSelector("[data-test-id='issue.activity.comment']"))
                ))

        return this;
    }

    override fun closePreviewIssue() {
        CloudClassicBoardPage(driver, issueSelector).closePreviewIssue()
    }

    override fun configure(): ConfigureBoard {
        return CloudConfigureBoard(driver)
    }

    private fun findIssueCards(): List<WebElement> {
        return driver.findElements(issueSelector)
    }

    private fun closeModals() {
        driver
            .findElements(By.cssSelector("[aria-label='Close Modal']"))
            .forEach { it.click() }
    }

    private class NextGenBoardContent(
        private val issueCards: List<WebElement>
    ) : BoardContent {

        override fun getIssueCount(): Int = issueCards.size

        /**
         * The new front-end is hostile towards automatic data discovery, so we give up.
         */
        override fun getIssueKeys(): Collection<String> = emptyList()
    }
}
