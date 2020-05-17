package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.VIEW_BOARD
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import jces1209.vu.MeasureType.Companion.ISSUE_BENTO_VIEW_BOARD
import jces1209.vu.page.TipPageComponent
import jces1209.vu.page.boards.cloud.BoardPage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ViewCloudBoard(
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val boardMemory: Memory<BoardPage>,
    private val issueKeyMemory: IssueKeyMemory,
    private val random: SeededRandom,
    private val viewIssueProbability: Float,
    private val tipsPageComponent: TipPageComponent
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    constructor(
        jira: WebJira,
        meter: ActionMeter,
        boardMemory: Memory<BoardPage>,
        issueKeyMemory: IssueKeyMemory,
        random: SeededRandom,
        viewIssueProbability: Float
    ) : this(
        jira = jira,
        meter = meter,
        boardMemory = boardMemory,
        issueKeyMemory = issueKeyMemory,
        random = random,
        viewIssueProbability = viewIssueProbability,
        tipsPageComponent = TipPageComponent(jira.driver)
    )

    override fun run() {
        val board = boardMemory.recall()
        if (board == null) {
            logger.debug("I cannot recall any board, skipping...")
            return
        }

        meter.measure(
            key = VIEW_BOARD,
            action = {
                jira.driver.navigate().to(board.uri.toURL())
                board.waitForBoardPageToLoad()
            },
            observation = { boardContent ->
                issueKeyMemory.remember(boardContent.getIssueKeys())
                IssuesOnBoard(boardContent.getIssueCount()).serialize()
            }
        )

        if (random.random.nextFloat() < viewIssueProbability) {
            meter.measure(ISSUE_BENTO_VIEW_BOARD) {
                if (board.areThereIssues()) {
                    tipsPageComponent.closeTips()
                    board.issueBentoView()
                }
            }
        }
    }
}
