package jces1209.vu.action;

import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import jces1209.vu.MeasureType.Companion.CUSTOMIZE_COLUMNS
import jces1209.vu.page.customizecolumns.ColumnsEditor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class CustomizeColumns(
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val columnsEditor: ColumnsEditor
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun run() {
        jira.goToIssueNavigator("resolution = Unresolved ORDER BY priority DESC")
        meter.measure(
            key = CUSTOMIZE_COLUMNS,
            action = {
                columnsEditor.openEditor()
                columnsEditor.selectItems(2)
                columnsEditor.submitSelection()
            }
        )
    }
}
