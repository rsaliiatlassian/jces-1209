package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import jces1209.vu.MeasureType
import jces1209.vu.page.admin.ProjectRoles.ProjectRolesPage
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class ProjectRoles(
    private val meter: ActionMeter,
    private val projectRolesPage: ProjectRolesPage
) : Action {

    override fun run() {
        meter.measure(MeasureType.PROJECT_ROLES) {
            projectRolesPage
                .open()
                .waitForBeingLoaded()
        }
    }
}
