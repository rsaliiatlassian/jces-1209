package jces1209.vu.page

import com.atlassian.performance.tools.jiraactions.api.page.wait
import com.atlassian.performance.tools.jirasoftwareactions.webdriver.JavaScriptUtils
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration

class DcViewBoard(
    private val driver: WebDriver
) {
    fun waitForBoard() = driver.wait(
        Duration.ofSeconds(30),
        presenceOfElementLocated(By.cssSelector(".ghx-column"))
    )

    fun getIssueKeys(): List<String> {
        return JavaScriptUtils.executeScript(driver,
            "return Array.from(document.getElementsByClassName('ghx-issue'), i => i.getAttribute('data-issue-key'))"
        )
    }

    fun openIssueBentoView() {
        driver
            .wait(visibilityOfElementLocated(By.className("ghx-issue")))
            .click()

        driver
            .wait(
                and(
                    visibilityOfElementLocated(By.id("ghx-detail-issue")),
                    visibilityOfAllElementsLocatedBy(By.className("issue-drop-zone"))
                ))
    }
}
