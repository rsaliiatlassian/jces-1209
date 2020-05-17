package jces1209.vu.page

import jces1209.vu.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions

class TipPageComponent(
    private val driver: WebDriver
) {

    fun closeTips() {
        driver
            .findElements(By.className("jira-help-tip"))
            .stream()
            .forEach {
                it
                    .findElement(By.className("helptip-close"))
                    .click()
                driver.wait(ExpectedConditions.invisibilityOf(it))
            }
    }
}
