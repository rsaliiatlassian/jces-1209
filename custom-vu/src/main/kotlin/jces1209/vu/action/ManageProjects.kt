package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.action.Action
import jces1209.vu.Measure
import jces1209.vu.MeasureType
import jces1209.vu.page.admin.manageprojects.ManageProjectsPage

class ManageProjects (
    private val measure: Measure,
    private val manageProjectsPage: ManageProjectsPage
) : Action {

    override fun run() {
        measure.measure(MeasureType.MANAGE_PROJECTS) {
            manageProjectsPage
                .open()
                .waitForBeingLoaded()
        }
    }
}
