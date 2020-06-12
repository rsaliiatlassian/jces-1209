package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.BROWSE_BOARDS
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import jces1209.vu.page.boards.browse.BoardList
import jces1209.vu.page.boards.browse.BrowseBoardsPage
import jces1209.vu.page.boards.view.BoardPage
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class BrowseBoards(
    private val jira: WebJira,
    private val browseBoardsPage: BrowseBoardsPage,
    private val meter: ActionMeter,
    private val kanbanBoardsMemory: Memory<BoardPage>,
    private val scrumBoardsMemory: Memory<BoardPage>,
    private val nextGenBoardsMemory: Memory<BoardPage>
) : Action {

    override fun run() {
        val boardList = meter.measure(BROWSE_BOARDS) {
            jira.navigateTo("secure/ManageRapidViews.jspa")
            browseBoardsPage
                .waitForBoards()
        }
        val listBoards = boardList.listBoards()
        listBoards[BoardList.boardNameKanban]?.let { kanbanBoardsMemory.remember(it) }
        listBoards[BoardList.boardNameScrum]?.let { scrumBoardsMemory.remember(it) }
        listBoards[BoardList.boardNameNextGen]?.let { nextGenBoardsMemory.remember(it) }
    }
}
