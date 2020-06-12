package jces1209.vu.memory

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import jces1209.vu.page.boards.browse.BoardList
import jces1209.vu.page.boards.view.*

class BoardPagesMemory(
    val seededRandom: SeededRandom
) {
    val all = SeededMemory<BoardPage>(seededRandom)
    val kanban = SeededMemory<KanbanBoardPage>(seededRandom)
    val backlog = SeededMemory<ScrumBacklogPage>(seededRandom)
    val sprint = SeededMemory<ScrumSprintPage>(seededRandom)
    val nextGen = SeededMemory<NextGenBoardPage>(seededRandom)

    fun rememberBoardsList(boardsList: BoardList.MixedBoards) {
        all.remember(boardsList.kanban)
        all.remember(boardsList.backlog)
        all.remember(boardsList.sprint)
        all.remember(boardsList.nextGen)

        kanban.remember(boardsList.kanban)
        backlog.remember(boardsList.backlog)
        sprint.remember(boardsList.sprint)
        nextGen.remember(boardsList.nextGen)
    }
}
