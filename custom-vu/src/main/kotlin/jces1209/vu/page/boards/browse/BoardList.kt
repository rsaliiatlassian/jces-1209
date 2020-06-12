package jces1209.vu.page.boards.browse

import jces1209.vu.page.boards.view.BoardPage

abstract class BoardList {

    /**
     * @return key - board type, value - list of board pages
     */
    abstract fun listBoards(): Map<String, Collection<BoardPage>>

    companion object {
        const val boardNameKanban = "Kanban"
        const val boardNameScrum = "Scrum"
        const val boardNameNextGen = "Next-gen"
    }
}
