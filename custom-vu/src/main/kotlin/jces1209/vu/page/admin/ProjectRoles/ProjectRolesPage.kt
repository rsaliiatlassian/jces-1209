package jces1209.vu.page.admin.ProjectRoles

import com.atlassian.performance.tools.jiraactions.api.WebJira

abstract class ProjectRolesPage(
    private val jira: WebJira
) {

    fun open(): ProjectRolesPage {
        jira.navigateTo("secure/project/ViewProjectRoles.jspa")
        return this
    }

    abstract fun waitForBeingLoaded(): ProjectRolesPage
}
