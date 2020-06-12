package jces1209.vu

import com.atlassian.performance.tools.jiraactions.api.ActionType

class MeasureType {

    companion object {
        @JvmField
        val CONTEXT_OPERATION_BOARD = ActionType("Context operation (Board)") { Unit }

        @JvmField
        val CONTEXT_OPERATION_ISSUE = ActionType("Context operation (Issue)") { Unit }

        @JvmField
        val CUSTOMIZE_COLUMNS = ActionType("Customize columns") { Unit }

        @JvmField
        val ISSUE_PREVIEW_BOARD = ActionType("Preview issue (Board)") { Unit }

        @JvmField
        val ISSUE_EDIT_DESCRIPTION = ActionType("Edit Issue Description") { Unit }

        @JvmField
        val ISSUE_LINK = ActionType("Link Issue") { Unit }

        @JvmField
        val ISSUE_LINK_LOAD_FORM = ActionType("Link Issue(Load form)") { Unit }

        @JvmField
        val ISSUE_LINK_SEARCH_CHOOSE = ActionType("Link Issue(Search issue and choose)") { Unit }

        @JvmField
        val CREATE_DASHBOARD = ActionType("Create Dashboard") { Unit }

        @JvmField
        val CREATE_GADGET = ActionType("Create Gadget") { Unit }

        @JvmField
        val LOAD_GADGET = ActionType("Load Gadget") { Unit }

        @JvmField
        val ISSUE_LINK_SUBMIT = ActionType("Link Issue(Submit)") { Unit }

        @JvmField
        val SPRINT_CREATE = ActionType("Sprint (Create)") { Unit }

        @JvmField
        val SPRINT_COMPLETE = ActionType("Sprint (Complete)") { Unit }

        @JvmField
        val SPRINT_MOVE_ISSUE = ActionType("Sprint (Move Issue In Sprint)") { Unit }

        @JvmField
        val SPRINT_REORDER_ISSUE = ActionType("Sprint (Reorder issue)") { Unit }

        @JvmField
        val SPRINT_START_SPRINT = ActionType("Sprint (Start sprint)") { Unit }

        @JvmField
        val SPRINT_START_SPRINT_EDITOR = ActionType("Sprint (Start sprint (open pop up))") { Unit }

        @JvmField
        val SPRINT_START_SPRINT_SUBMIT = ActionType("Sprint (Start sprint (submit))") { Unit }

        @JvmField
        val ATTACH_SCREENSHOT= ActionType("Attach screenshot") { Unit }

        @JvmField
        val CONFIGURE_BOARD = ActionType("Configure board") { Unit }

        @JvmField
        val CREATE_ISSUE_MODAL = ActionType("Create Issue Modal") { Unit }

        @JvmField
        val SWITCH_BETWEEN_ISSUES_IN_SEARCH_RESULTS = ActionType("Switch between issues in search results") { Unit }

        @JvmField
        var TOP_BAR_QUICK_SEARCH = ActionType("Quick search top bar") { Unit }

        @JvmField
        val TRANSITION = ActionType("Transition") { Unit }

        @JvmField
        val TRANSITION_FILL_IN_TIME_SPENT_FORM = ActionType("Transition (Fill in Time Spent form)") { Unit }

        @JvmField
        val BROWSE_PROJECT_ISSUES= ActionType("Browse project issues") { Unit }

        @JvmField
        var OPEN_MEDIA_VIEWER = ActionType("Quick search top bar") { Unit }
    }
}
