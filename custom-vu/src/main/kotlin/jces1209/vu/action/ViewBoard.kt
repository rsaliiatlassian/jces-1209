package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.VIEW_BOARD
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import jces1209.vu.MeasureType
import jces1209.vu.page.JiraTips
import jces1209.vu.page.boards.browse.BoardList
import jces1209.vu.page.boards.view.BoardPage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ViewBoard(
    private val meter: ActionMeter,
    private val kanbanBoardMemory: Memory<BoardPage>,
    private val scrumBoardMemory: Memory<BoardPage>,
    private val nextGenBoardMemory: Memory<BoardPage>,
    private val issueKeyMemory: IssueKeyMemory,
    private val random: SeededRandom,
    private val viewIssueProbability: Float,
    private val jiraTips: JiraTips
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun run() {
        for ((boardType, boardMemory) in mapOf(
            BoardList.boardNameKanban to kanbanBoardMemory,
            BoardList.boardNameScrum to scrumBoardMemory,
            BoardList.boardNameNextGen to nextGenBoardMemory)) {

            val board = boardMemory.recall()
            if (board == null) {
                logger.debug("I cannot recall [$boardType] board, skipping...")
                return
            }
            val boardContent = meter.measure(
                key = VIEW_BOARD,
                action = {
                    meter.measure(
                        key = ActionType("View Board ($boardType)") { Unit },
                        action = {
                            board
                                .goToBoard()
                                .waitForBoardPageToLoad()
                        },
                        observation = {
                            issueKeyMemory.remember(it.getIssueKeys())
                            IssuesOnBoard(it.getIssueCount()).serialize()
                        }
                    )
                }
            )

            if (random.random.nextFloat() < viewIssueProbability) {
                if (boardContent.getIssueKeys().isEmpty()) {
                    logger.debug("It requires some issues on board to test preview issue")
                } else {
                    jiraTips.closeTips()
                    meter.measure(MeasureType.ISSUE_PREVIEW_BOARD) {
                        meter.measure(ActionType("Preview issue ($boardType board)") { Unit }) {
                            board.previewIssue()
                        }
                    }
                }
            }
        }
    }
}
