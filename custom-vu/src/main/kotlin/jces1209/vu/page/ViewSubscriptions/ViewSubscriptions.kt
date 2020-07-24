package jces1209.vu.page.ViewSubscriptions

import com.atlassian.performance.tools.jiraactions.api.WebJira
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.and
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated

abstract class ViewSubscriptions(
    protected val jira: WebJira
) {
    protected val filterSubscriptionList = and(
        presenceOfElementLocated(By.xpath("//*[. = 'Subscriptions']")),
        presenceOfElementLocated(By.xpath("//*[. = 'View this filter']")),
        presenceOfElementLocated(By.xpath("//*[contains(text(), 'Add subscription')]")),
        presenceOfElementLocated(By.id("edit_subscription"))
    )

    abstract fun waitForLoad(): ViewSubscriptions
}
