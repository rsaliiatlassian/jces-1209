package jces1209.vu.page.ViewSubscriptions.cloud

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.FalliblePage
import jces1209.vu.page.ViewSubscriptions.ViewSubscriptions
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import java.time.Duration

class CloudViewSubscriptions(
    jira: WebJira
): ViewSubscriptions(jira) {
    private val filterSubscriptionFalliblePage = FalliblePage.Builder(
        jira.driver,
        ExpectedConditions.and(
            filterSubscriptionList,
            ExpectedConditions.presenceOfElementLocated(By.xpath("//*[. = 'Delete']")),
            ExpectedConditions.presenceOfElementLocated(By.xpath("//*[. = 'Run now']"))
        )
    )
        .cloudErrors()
        .timeout(Duration.ofSeconds(30))
        .build()

    override fun waitForLoad(): ViewSubscriptions {
        filterSubscriptionFalliblePage.waitForPageToLoad()
        return this
    }
}
