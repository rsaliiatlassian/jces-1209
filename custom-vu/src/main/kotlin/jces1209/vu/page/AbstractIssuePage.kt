package jces1209.vu.page

interface AbstractIssuePage {

    fun waitForSummary(): AbstractIssuePage
    fun comment(): Commenting
    fun editDescription(description: String): AbstractIssuePage
    fun linkIssue(): IssueLinking
    fun transition(): AbstractIssuePage
    fun isTimeSpentFormAppeared(): Boolean
    fun fillInTimeSpentForm(): AbstractIssuePage
    fun cancelTimeSpentForm(): AbstractIssuePage
}
