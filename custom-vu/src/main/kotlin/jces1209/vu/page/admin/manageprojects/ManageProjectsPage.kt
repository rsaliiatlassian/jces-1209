package jces1209.vu.page.admin.manageprojects

import com.atlassian.performance.tools.jiraactions.api.WebJira
import jces1209.vu.page.FalliblePage

abstract class ManageProjectsPage(
    private val jira: WebJira
) {
    abstract val falliblePage: FalliblePage

    fun open(): ManageProjectsPage {
        jira.navigateTo("secure/project/ViewProjects.jspa")
        return this
    }

    fun waitForBeingLoaded(): ManageProjectsPage {
        falliblePage.waitForPageToLoad()
        return this
    }
}
