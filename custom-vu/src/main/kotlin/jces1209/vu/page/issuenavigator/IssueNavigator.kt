package jces1209.vu.page.issuenavigator

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.FalliblePage
import jces1209.vu.page.issuenavigator.bulkoperation.BulkOperationPage
import jces1209.vu.page.ViewSubscriptions.ViewSubscriptions
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.*

abstract class IssueNavigator(
    protected val jira: WebJira
) {
    enum class ViewType {
        DETAIL, LIST
    }

    protected val driver = jira.driver
    protected val filterSubscriptionList = and(
        presenceOfElementLocated(By.id("filter-subscription")),
        presenceOfElementLocated(By.id("filter.subscription.prefix.daysOfMonth")),
        presenceOfElementLocated(By.className("form-body")),
        presenceOfElementLocated(By.xpath("//*[. = 'Recipients']")),
        presenceOfElementLocated(By.xpath("//*[. = 'Schedule']")),
        presenceOfElementLocated(By.xpath("//*[. = 'Interval']")),
        presenceOfElementLocated(By.id("filter-subscription-submit")),
        presenceOfElementLocated(By.id("filter-subscription-cancel"))
    )
    protected val filterDetailsLocator = By.className("show-filter-details")
    protected abstract val filterSubscriptionFalliblePage: FalliblePage
    protected abstract val viewSubscriptions: ViewSubscriptions

    private val changeViewButtonLocator = By.id("layout-switcher-button")

    abstract fun waitForBeingLoaded(): IssueNavigator
    abstract fun selectIssue()
    abstract fun openBulkOperation(): BulkOperationPage


    fun openChangeViewPopup(): IssueNavigator {
        driver
            .wait(elementToBeClickable(changeViewButtonLocator))
            .click()
        driver.wait(
            visibilityOfNestedElementsLocatedBy(By.cssSelector("#layout-switcher-button_drop, #layout-switcher-options"), By.className("layout-switcher-item"))
        )
        return this
    }

    fun getViewType(): ViewType {
        val switchButton = driver.wait(ExpectedConditions.visibilityOfElementLocated(changeViewButtonLocator))

        return when {
            switchButton.findElements(By.className("aui-iconfont-view-list")).isNotEmpty() -> {
                ViewType.DETAIL
            }
            switchButton.findElements(By.className("aui-iconfont-view-table")).isNotEmpty() -> {
                ViewType.LIST
            }
            else -> {
                throw RuntimeException("Unexpected switch view button type")
            }
        }
    }

    fun selectInactiveViewType(): IssueNavigator {
        val issues = driver.findElements(By.cssSelector(".issue-link-key, .issuekey"))
        driver
            .wait(elementToBeClickable(By.cssSelector(".layout-switcher-item > .aui-list-item-link > :not(.aui-iconfont-success)")))
            .click()
        driver.wait(invisibilityOfAllElements(issues))
        waitForBeingLoaded()
        return this
    }

    fun clickNewSubscription() {
        driver.wait(
            elementToBeClickable(By.xpath("//*[@id = 'inline-dialog-filter-details-overlay']//*[contains(text(), 'New subscription')]"))
        )
            .click()
        filterSubscriptionFalliblePage.waitForPageToLoad()
    }

    open fun clickDetails() {
        driver.wait(
            elementToBeClickable(filterDetailsLocator)
        )
            .click()
        waitForDetailsPopup()
    }

    fun openNavigator(): IssueNavigator {
        jira.goToIssueNavigator("resolution = Unresolved ORDER BY priority DESC")
        return this;
    }

    fun subscribe() {
        val submitLocator = By.id("filter-subscription-submit")
        driver.wait(
            elementToBeClickable(submitLocator)
        )
            .click()

        driver.wait(
            invisibilityOfElementLocated(submitLocator)
        )
        waitForBeingLoaded()
    }

    fun manageSubscriptions(): ViewSubscriptions {
        driver.wait(
            elementToBeClickable(By.xpath("//*[@class = 'manage-links']//a[. = 'Manage subscriptions']"))
        )
            .click()
        viewSubscriptions.waitForLoad()
        return viewSubscriptions
    }

    protected fun waitForDetailsPopup() {
        val detailsView = driver.wait(
            visibilityOfElementLocated(By.id("inline-dialog-filter-details-overlay")))
        driver.wait(
            and(
                visibilityOfNestedElementsLocatedBy(detailsView, By.className("search-owner")),
                visibilityOfNestedElementsLocatedBy(detailsView, By.className("search-owner-name")),
                visibilityOfNestedElementsLocatedBy(detailsView, By.className("filter-details-section")),
                visibilityOfNestedElementsLocatedBy(detailsView, By.className("manage-links")),
                visibilityOfNestedElementsLocatedBy(detailsView, By.className("edit-permissions"))
            )
        )
    }
}
