package jces1209.vu.page.boards.view.dc

import com.atlassian.performance.tools.jiraactions.api.page.wait
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.BoardPage
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import java.net.URI
import java.time.Duration

abstract class DcBoardPage(
    driver: WebDriver,
    uri: URI
) : BoardPage(
    driver = driver,
    uri = uri
) {
    override fun waitForBoardPageToLoad(): BoardContent {
        driver.wait(
            Duration.ofSeconds(30),
            ExpectedConditions.presenceOfElementLocated(issueSelector)
        )
        return GeneralBoardContent(driver, issueSelector)
    }

    override fun previewIssue(): DcBoardPage {
        driver
            .wait(ExpectedConditions.visibilityOfElementLocated(issueSelector))
            .click()

        driver
            .wait(
                ExpectedConditions.and(
                    ExpectedConditions.visibilityOfElementLocated(By.id("ghx-detail-issue")),
                    ExpectedConditions.visibilityOfElementLocated(By.className("issue-drop-zone"))
                ))

        return this;
    }
}
