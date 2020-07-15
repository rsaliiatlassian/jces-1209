package jces1209.vu.page.bulkOperation

import jces1209.vu.page.bars.topBar.TopBar

interface BulkOperation {
    fun waitForBulkOperationPage(): BulkOperation
    fun chooseIssues(issuesNumber: Int): BulkOperation
    fun chooseOperation(): BulkOperation
    fun operationDetails(): BulkOperation
    fun confirmation(): BulkOperation
    fun progress(): BulkOperation
    fun submit(): BulkOperation
}
