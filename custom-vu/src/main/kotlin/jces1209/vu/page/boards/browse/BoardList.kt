package jces1209.vu.page.boards.browse

import jces1209.vu.page.boards.view.KanbanBoardPage
import jces1209.vu.page.boards.view.NextGenBoardPage
import jces1209.vu.page.boards.view.ScrumBacklogPage
import jces1209.vu.page.boards.view.ScrumSprintPage

abstract class BoardList {

    abstract fun listBoards(): MixedBoards

    class MixedBoards(
        val kanban: Collection<KanbanBoardPage>,
        val backlog: Collection<ScrumBacklogPage>,
        val sprint: Collection<ScrumSprintPage>,
        val nextGen: Collection<NextGenBoardPage>
    )
}
