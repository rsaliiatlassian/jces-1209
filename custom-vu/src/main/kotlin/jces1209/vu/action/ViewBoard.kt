package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.VIEW_BOARD
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import jces1209.vu.MeasureType
import jces1209.vu.memory.SeededMemory
import jces1209.vu.page.JiraTips
import jces1209.vu.page.boards.view.BoardContent
import jces1209.vu.page.boards.view.BoardPage
import jces1209.vu.page.contextoperation.ContextOperationBoard
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.WebDriver

class ViewBoard(
    private val driver: WebDriver,
    private val meter: ActionMeter,
    private val boardsMemory: SeededMemory<BoardPage>,
    private val issueKeyMemory: IssueKeyMemory,
    private val random: SeededRandom,
    private val viewIssueProbability: Float,
    private val configureBoardProbability: Float,
    private val contextOperationProbability: Float
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)
    private val jiraTips = JiraTips(driver)

    override fun run() {
        val board = boardsMemory.recall()
        if (board == null) {
            logger.debug("I cannot recall board, skipping...")
            return
        }
        val boardType = board.getTypeLabel()
        val boardContent = viewBoard(boardType, board)

        if (random.random.nextFloat() < viewIssueProbability) {
            if (boardContent.getIssueKeys().isEmpty()) {
                logger.debug("It requires some issues on board to test preview issue")
            } else {
                previewIssue(boardType, board)
                contextOperation(boardType)
            }
        }
        jiraTips.closeTips()
        configureBoard(boardType, board)
    }

    private fun viewBoard(boardType: String, board: BoardPage): BoardContent {
        return meter.measure(
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
    }

    private fun contextOperation(boardType: String) {
        if (random.random.nextFloat() < contextOperationProbability) {
            meter.measure(MeasureType.CONTEXT_OPERATION_BOARD) {
                meter.measure(ActionType("Context operation ($boardType board)") { Unit }) {
                    ContextOperationBoard(driver)
                        .open()
                }
            }
                .close()
        }
    }

    private fun previewIssue(boardType: String, board: BoardPage) {
        jiraTips.closeTips()
        meter.measure(MeasureType.ISSUE_PREVIEW_BOARD) {
            meter.measure(ActionType("Preview issue ($boardType board)") { Unit }) {
                board.previewIssue()
            }
        }
        board.closePreviewIssue()
    }

    private fun configureBoard(boardType: String, board: BoardPage) {
        if (random.random.nextFloat() < configureBoardProbability) {
            meter.measure(MeasureType.CONFIGURE_BOARD) {
                meter.measure(ActionType(MeasureType.CONFIGURE_BOARD.label + " ($boardType board)") { Unit }) {
                    board
                        .configureBoard()
                        .waitForLoadPage()
                }
            }
        }
    }
}
