package jces1209.vu.page.bulkOperation.cloud

import jces1209.vu.page.FalliblePage
import jces1209.vu.page.bulkOperation.BulkOperation
import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import java.time.Duration

class CloudBulkOperation(
    private val driver: WebDriver
): BulkOperation {
    private val falliblePage = FalliblePage.Builder(
        webDriver = driver,
        expectedContent =
        ExpectedConditions.or(
            ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='stepped-process']//*[contains(text(), 'Choose Issues')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='stepped-process']//*[contains(text(), 'Choose Operation')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='stepped-process']//*[contains(text(), 'Operation Details')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='stepped-process']//*[contains(text(), 'Confirmation')]"))
            ),
            ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='stepped-process']//*[contains(text() , 'Bulk Operation Progress')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='stepped-process']//*[contains(text() , 'Bulk operation is 100% complete.')]"))
            )
        )
    )
        .cloudErrors()
        .timeout(Duration.ofSeconds(30))
        .build()

    private val blkEditComment = "Bulk Edit comment"

    override fun waitForBulkOperationPage(): BulkOperation {
        falliblePage.waitForPageToLoad()
        return this
    }

    //override fun chooseIssues(issuesNumber: Int): BulkOperation {
    override fun chooseIssues(issuesNumber: Int): BulkOperation {
        var issuesNumberLocal: Int = issuesNumber

        val checkboxElements =
            driver
                .wait(ExpectedConditions
                    .visibilityOfAllElementsLocatedBy(By.xpath("//*[@id = 'issuetable']//*[@class = 'issuerow']//input")))

        if (checkboxElements.size < issuesNumberLocal)
            issuesNumberLocal = checkboxElements.size

        if (issuesNumberLocal > 0)
            issuesNumberLocal--

        for (i in 0..issuesNumberLocal) {
            checkboxElements[i].click()
        }

        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("next")))
            .click()
        return this
    }

    override fun chooseOperation(): BulkOperation {
        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("bulk.edit.operation.name_id")))
            .click()
        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("next")))
            .click()
        return this
    }

    override fun operationDetails(): BulkOperation {
        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("cbcomment")))
            .click()
        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("comment")))
            .click()
        Actions(driver)
            .sendKeys(blkEditComment)
            .perform()
        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("next")))
            .click()
        return this
    }

    override fun confirmation(): BulkOperation {
        driver
            .wait(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//*[@id='updatedfields']//*[. = 'Comment']")))
        driver
            .wait(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//*[@id='updatedfields']//*[. = 'Add new']")))
        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("confirm")))
            .click()
        return this
    }

    override fun progress(): BulkOperation {
        driver
            .wait(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//*[@id='stepped-process']//*[contains(text() , 'Bulk Operation Progress')]")))
        driver
            .wait(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//*[@id='stepped-process']//*[contains(text() , 'Bulk operation is 100% complete.')]")))
        return this
    }

    override fun submit(): BulkOperation {
        driver
            .wait(ExpectedConditions
                .elementToBeClickable(By.id("acknowledge_submit")))
            .click()
        return this
    }
}
