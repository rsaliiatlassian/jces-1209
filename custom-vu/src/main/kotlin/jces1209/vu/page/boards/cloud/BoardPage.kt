package jces1209.vu.page.boards.cloud

import java.net.URI

interface BoardPage {

    val uri: URI
    fun waitForBoardPageToLoad(): BoardContent
    fun areThereIssues(): Boolean

    /**
     * Board must have issues
     */
    fun issueBentoView(): BoardPage
}
