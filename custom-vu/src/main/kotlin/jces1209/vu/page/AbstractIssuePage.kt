package jces1209.vu.page

interface AbstractIssuePage {

    fun waitForSummary(): AbstractIssuePage
    fun comment(): Commenting
    fun editDescription(description: String): AbstractIssuePage
    fun linkIssue(): IssueLinking
    fun changeAssignee(): AbstractIssuePage
}
