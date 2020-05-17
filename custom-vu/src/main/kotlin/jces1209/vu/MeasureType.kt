package jces1209.vu

import com.atlassian.performance.tools.jiraactions.api.ActionType

class MeasureType {
    companion object {
        @JvmField
        val ISSUE_BENTO_VIEW_BOARD = ActionType("Issue Bento view (Board)") { Unit }
        @JvmField
        val ISSUE_EDIT_DESCRIPTION = ActionType("Edit Issue Description") { Unit }
        @JvmField
        val ISSUE_LINK = ActionType("Link Issue") { Unit }
        @JvmField
        val ISSUE_LINK_LOAD_FORM = ActionType("Link Issue(Load form)") { Unit }
        @JvmField
        val ISSUE_LINK_SEARCH_CHOOSE= ActionType("Link Issue(Search issue and choose)") { Unit }
        @JvmField
        val ISSUE_LINK_SUBMIT= ActionType("Link Issue(Submit)") { Unit }
    }
}
