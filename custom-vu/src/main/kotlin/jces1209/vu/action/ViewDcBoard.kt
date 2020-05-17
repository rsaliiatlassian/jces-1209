package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.VIEW_BOARD
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.BoardMemory
import jces1209.vu.MeasureType.Companion.ISSUE_BENTO_VIEW_BOARD
import jces1209.vu.WebJiraSoftware
import jces1209.vu.page.TipPageComponent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ViewDcBoard(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardMemory: BoardMemory<AgileBoard>,
    private val issueKeyMemory: IssueKeyMemory,
    private val filter: (AgileBoard) -> Boolean,
    private val random: SeededRandom,
    private val viewIssueProbability: Float,
    private val tipsPageComponent: TipPageComponent
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardMemory: BoardMemory<AgileBoard>,
        issueKeyMemory: IssueKeyMemory,
        random: SeededRandom,
        viewIssueProbability: Float
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardMemory = boardMemory,
        issueKeyMemory = issueKeyMemory,
        filter = { true },
        random = random,
        viewIssueProbability = viewIssueProbability,
        tipsPageComponent = TipPageComponent(jiraSoftware.getDriver())
    )

    override fun run() {
        val board = boardMemory.recall(filter)

        if (board == null) {
            logger.debug("Skipping View Board. I have no knowledge of Boards.")
        } else {
            val boardPage = meter.measure(
                key = VIEW_BOARD,
                action = {
                    val issueBoard = jiraSoftware.goToViewBoard(board.id)
                    issueBoard.waitForBoard()
                    issueBoard
                },
                observation = { issueBoard ->
                    val issueKeys = issueBoard.getIssueKeys()
                    issueKeyMemory.remember(issueKeys)
                    board.issuesOnBoard = issueKeys.size
                    IssuesOnBoard(issues = issueKeys.size).serialize()
                }
            )

            if (random.random.nextFloat() < viewIssueProbability) {
                tipsPageComponent.closeTips()

                meter.measure(ISSUE_BENTO_VIEW_BOARD) {
                    boardPage.openIssueBentoView()
                }
            }
        }
    }
}
