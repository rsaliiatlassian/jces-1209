package jces1209.vu.page.bars.topBar.cloud

import jces1209.vu.page.FalliblePage
import jces1209.vu.page.bars.topBar.TopBar
import jces1209.vu.page.bars.topBar.dc.DcTopBar
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions

class CloudTopBar(
    private val driver: WebDriver
) : TopBar {

    private val page = FalliblePage.Builder(
        webDriver = driver,
        expectedContent = listOf(
            By.xpath("//*[@data-test-id='search-dialog-input']"),
            By.xpath("//*[@data-test-id='common.components.field-search']"),
            By.xpath("//*[@data-test-id='global-pages.directories.directory-base.content.table.container']"),
            By.xpath("//*[@data-test-id='atlassian-navigation--secondary-actions--profile--menu-trigger']")
        )
    )
        .cloudErrors()
        .build()

    override fun waitForTopBar(): CloudTopBar {
        page.waitForPageToLoad()
        return this
    }

    override fun quickSearch(): CloudTopBar {
        Actions(driver)
            .sendKeys("/")
            .perform()

        driver
            .wait(
                ExpectedConditions.and(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-test-id='search-dialog-advanced-search-link']")),
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-test-id='search-dialog-dialog-wrapper']//*[. = 'Recently viewed issues']")),
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-test-id='search-dialog-dialog-wrapper']//span[@title]")),
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-test-id='search-dialog-dialog-wrapper']//*[. = 'Boards, Projects and Filters']"))
                )
            )
        return this
    }
}
