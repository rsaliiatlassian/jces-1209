package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.*
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import jces1209.vu.page.AbstractIssuePage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Works for both Cloud and Data Center.
 */
class WorkAnIssue(
    private val issuePage: AbstractIssuePage,
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val issueKeyMemory: IssueKeyMemory,
    private val random: SeededRandom,
    private val editProbability: Float,
    private val commentProbability: Float,
    private val linkIssueProbability: Float,
    private val changeAssigneeProbability: Float,
    private val transitionProbability: Float,
    private val mentionUserProbability: Float
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun run() {
        val issueKey = issueKeyMemory.recall()
        if (issueKey == null) {
            logger.debug("I don't recall any issue keys. Maybe next time I will.")
            return
        }
        val loadedIssuePage = read(issueKey)
        if (random.random.nextFloat() < editProbability) {
            edit(loadedIssuePage)
        }
        if (random.random.nextFloat() < linkIssueProbability) {
            // issue must have other issues in a project to link them
            linkIssue(loadedIssuePage, issueKey.substringBefore("-"))
        }
        if (random.random.nextFloat() < commentProbability) {
            comment(loadedIssuePage)
        }
        if (random.random.nextFloat() < changeAssigneeProbability) {
            changeAssignee(loadedIssuePage)
        }
        if (random.random.nextFloat() < transitionProbability) {
            transition(loadedIssuePage)
        }
        if (random.random.nextFloat() < mentionUserProbability) {
            mentionUser(loadedIssuePage)
        }
    }

    private fun read(
        issueKey: String
    ): AbstractIssuePage = meter.measure(VIEW_ISSUE) {
        jira.goToIssue(issueKey)
        issuePage.waitForSummary()
    }

    private fun edit(issuePage: AbstractIssuePage) {
        meter.measure(ActionType("Edit Issue Description") { Unit }) {
            issuePage.editDescription("updated")
        }
        logger.debug("I want to edit the $issuePage")
    }

    private fun linkIssue(issuePage: AbstractIssuePage, issuePrefixSearch: String) {
        val issueLinking = issuePage.linkIssue()
        meter.measure(ActionType("Link Issue") { Unit }) {
            meter.measure(ActionType("Link Issue(Load form)") { Unit }) {
                issueLinking.openEditor()
            }
            meter.measure(ActionType("Link Issue(Search issue and choose)") { Unit }) {
                issueLinking.searchAndChooseIssue(issuePrefixSearch)
            }
            meter.measure(ActionType("Link Issue(Submit)") { Unit }) {
                issueLinking.submitIssue()
            }
        }
    }

    private fun comment(issuePage: AbstractIssuePage) {
        val commenting = issuePage.comment()
        meter.measure(ADD_COMMENT) {
            commenting.openEditor()
            commenting.typeIn("abc def")
            meter.measure(ADD_COMMENT_SUBMIT) {
                commenting.saveComment()
                commenting.waitForTheNewComment()
            }
        }
    }

    private fun changeAssignee(issuePage: AbstractIssuePage) {
        meter.measure(ActionType("Change Assignee") {Unit}) {
            issuePage.changeAssignee()
        }
    }

    private fun transition(issuePage: AbstractIssuePage) {
        meter.measure(ActionType("Transition") {Unit}) {
            issuePage.transition()
        }
    }

    //private fun mentionUser(issuePage: AbstractIssuePage, user: String) {
    private fun mentionUser(issuePage: AbstractIssuePage) {
        val commenting = issuePage.comment()
        meter.measure(ActionType("Mention a user") {Unit}) {
            commenting.openEditor()
            commenting.typeIn("abc def ")
            commenting.mentionUser()
            meter.measure(ADD_COMMENT_SUBMIT) {
                commenting.saveComment()
                commenting.waitForTheNewComment()
            }
        }
    }
}
