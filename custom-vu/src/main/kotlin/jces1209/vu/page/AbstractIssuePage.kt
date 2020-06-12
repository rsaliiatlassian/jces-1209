package jces1209.vu.page

import jces1209.vu.page.contextoperation.ContextOperationIssue

interface AbstractIssuePage {

    fun waitForSummary(): AbstractIssuePage
    fun comment(): Commenting
    fun editDescription(description: String): AbstractIssuePage
    fun linkIssue(): IssueLinking
    fun transition(): AbstractIssuePage
    fun isTimeSpentFormAppeared(): Boolean
    fun fillInTimeSpentForm(): AbstractIssuePage
    fun addAttachment(): AttachScreenShot
    fun changeAssignee(): AbstractIssuePage
    fun contextOperation(): ContextOperationIssue
    fun cancelTimeSpentForm(): AbstractIssuePage
}
