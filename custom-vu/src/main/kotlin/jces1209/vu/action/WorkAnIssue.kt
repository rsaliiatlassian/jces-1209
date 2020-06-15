package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.*
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import jces1209.vu.MeasureType
import jces1209.vu.MeasureType.Companion.ATTACH_SCREENSHOT
import jces1209.vu.MeasureType.Companion.CONTEXT_OPERATION_ISSUE
import jces1209.vu.MeasureType.Companion.ISSUE_EDIT_DESCRIPTION
import jces1209.vu.MeasureType.Companion.ISSUE_LINK
import jces1209.vu.MeasureType.Companion.ISSUE_LINK_LOAD_FORM
import jces1209.vu.MeasureType.Companion.ISSUE_LINK_SEARCH_CHOOSE
import jces1209.vu.MeasureType.Companion.ISSUE_LINK_SUBMIT
import jces1209.vu.MeasureType.Companion.OPEN_MEDIA_VIEWER
import jces1209.vu.page.AbstractIssuePage
import jces1209.vu.page.AttachScreenShot
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
    private val attachScreenShotProbability: Float,
    private val changeAssigneeProbability: Float,
    private val mentionUserProbability: Float,
    private val contextOperationProbability: Float,
    private val transitionProbability: Float
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
        if (roll(attachScreenShotProbability)) {
            attachScreenShot(loadedIssuePage)
            openScreenShot()
        }
        if (random.random.nextFloat() < changeAssigneeProbability) {
            changeAssignee(loadedIssuePage)
        }
        if (random.random.nextFloat() < mentionUserProbability) {
            mentionUser(loadedIssuePage)
        }
        if (random.random.nextFloat() < contextOperationProbability) {
            contextOperation(loadedIssuePage)
        }
        if (random.random.nextFloat() < transitionProbability) {
            transition(loadedIssuePage)
        }
    }

    private fun roll(
        probability: Float
    ): Boolean = (random.random.nextFloat() < probability)

    private fun read(
        issueKey: String
    ): AbstractIssuePage = meter.measure(VIEW_ISSUE) {
        jira.goToIssue(issueKey)
        issuePage.waitForSummary()
    }

    private fun edit(issuePage: AbstractIssuePage) {
        meter.measure(ISSUE_EDIT_DESCRIPTION) {
            issuePage.editDescription("updated")
        }
        logger.debug("I want to edit the $issuePage")
    }

    private fun linkIssue(issuePage: AbstractIssuePage, issuePrefixSearch: String) {
        val issueLinking = issuePage.linkIssue()
        if (issueLinking.isLinkButtonPresent()) {
            meter.measure(ISSUE_LINK) {
                meter.measure(ISSUE_LINK_LOAD_FORM) {
                    issueLinking.openEditor()
                }
                meter.measure(ISSUE_LINK_SEARCH_CHOOSE) {
                    issueLinking.searchAndChooseIssue(issuePrefixSearch)
                }
                meter.measure(ISSUE_LINK_SUBMIT) {
                    issueLinking.submitIssue()
                }
            }
        } else {
            logger.debug("Issue doesn't have link button")
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

    private fun attachScreenShot(issuePage: AbstractIssuePage) {
        val attachScreenShot = issuePage.addAttachment()
        attachScreenShot.makeScreenShot()
        meter.measure(ATTACH_SCREENSHOT) {
            attachScreenShot.pasteScreenShot()
        }
    }

    private fun openScreenShot() {
        meter.measure(OPEN_MEDIA_VIEWER) {
            issuePage.addAttachment().openScreenShot()
        }
            .closeMediaViewModal()
    }

    private fun mentionUser(issuePage: AbstractIssuePage) {
        val commenting = issuePage.comment()
        meter.measure(ActionType("Mention a user") { Unit }) {
            commenting.openEditor()
            commenting.typeIn("abc def ")
            commenting.mentionUser()
            meter.measure(ADD_COMMENT_SUBMIT) {
                commenting.saveComment()
                commenting.waitForTheNewComment()
            }
        }
    }

    private fun changeAssignee(issuePage: AbstractIssuePage) {
        meter.measure(ActionType("Change Assignee") { Unit }) {
            issuePage.changeAssignee()
        }
    }

    private fun contextOperation(issuePage: AbstractIssuePage) {
        meter.measure(CONTEXT_OPERATION_ISSUE) {
            issuePage
                .contextOperation()
                .open()
        }
            .close()
    }

    private fun transition(issuePage: AbstractIssuePage) {
        logger.info("transition start")
        issuePage.transition()
        //TODO("use ExpectedConditions.or() instead of isTimeSpentFormAppeared")
        val isTimeSpentFormAppeared = issuePage.isTimeSpentFormAppeared()
        if (isTimeSpentFormAppeared)
            issuePage.cancelTimeSpentForm()

        meter.measure(MeasureType.TRANSITION) {
            issuePage.transition()
            if (isTimeSpentFormAppeared) {
                meter.measure(MeasureType.TRANSITION_FILL_IN_TIME_SPENT_FORM) {
                    issuePage.fillInTimeSpentForm()
                }
            }
        }
    }
}
