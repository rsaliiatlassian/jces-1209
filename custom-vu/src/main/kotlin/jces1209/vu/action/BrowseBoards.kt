package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.BROWSE_BOARDS
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import jces1209.vu.memory.BoardPagesMemory
import jces1209.vu.page.boards.browse.BrowseBoardsPage
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class BrowseBoards(
    private val jira: WebJira,
    private val browseBoardsPage: BrowseBoardsPage,
    private val meter: ActionMeter,
    private val boardsMemory: BoardPagesMemory
) : Action {

    override fun run() {
        val boardList = meter.measure(BROWSE_BOARDS) {
            jira.navigateTo("secure/ManageRapidViews.jspa")
            browseBoardsPage
                .waitForBoards()
        }
        boardsMemory.rememberBoardsList(boardList.listBoards())
    }
}
