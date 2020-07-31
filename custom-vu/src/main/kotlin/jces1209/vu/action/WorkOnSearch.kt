package jces1209.vu.action

import com.atlassian.performance.tools.jiraactions.api.SEARCH_WITH_JQL
import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.JqlMemory
import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jiraactions.api.observation.SearchJqlObservation
import com.atlassian.performance.tools.jiraactions.api.page.IssueNavigatorPage
import jces1209.vu.MeasureType
import jces1209.vu.MeasureType.Companion.SWITCH_BETWEEN_ISSUES_IN_SEARCH_RESULTS
import jces1209.vu.page.IssueNavigator
import jces1209.vu.page.customizecolumns.ColumnsEditor
import java.lang.Exception
import java.net.URI
import javax.json.JsonObject

class WorkOnSearch(
    private val issueNavigator: IssueNavigator,
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val columnsEditor: ColumnsEditor,
    private val random: SeededRandom,
    private val filters: Memory<URI>,
    private val jqlMemory: JqlMemory,
    private val issueKeyMemory: IssueKeyMemory,
    private val searchFilterProbability: Float,
    private val searchJclProbability: Float,
    private val globalSearchProbability: Float,
    private val customizeColumnsProbability: Float,
    private val switchViewsProbability: Float,
    private val switchBetweenIssuesProbability: Float
) : Action {
    override fun run() {
        if (roll(switchViewsProbability)) {
            switchViews()
        }
        if (roll(globalSearchProbability)) {
            openGlobalIssueSearch()
        }
        if (roll(searchFilterProbability)) {
            searchFilter()
        }
        if (roll(searchJclProbability)) {
            searchJcl()
        }
        if (roll(customizeColumnsProbability)) {
            customizeColumns()
        }
        if (roll(switchBetweenIssuesProbability)) {
            switchBetweenIssues()
        }
    }

    private fun roll(
        probability: Float
    ): Boolean = (random.random.nextFloat() < probability)

    private fun switchViews() {
        issueNavigator
            .openNavigator()
            .waitForNavigator()
        meter.measure(
            key = MeasureType.SWITCH_VIEWS,
            action = {
                meter.measure(
                    key = MeasureType.SWITCH_VIEWS_CHANGE_VIEW_POPUP,
                    action = {
                        issueNavigator.changeViewPopup()
                    }
                )
                val viewType = issueNavigator.getViewType()
                if (viewType == IssueNavigator.ViewType.DETAIL) {
                    meter.measure(
                        key = MeasureType.SWITCH_VIEWS_CHANGE_VIEW_TYPE,
                        action = {
                            issueNavigator.changeViewType(IssueNavigator.ViewType.LIST)
                            issueNavigator.waitForNavigator()
                        }
                    )
                    meter.measure(
                        key = MeasureType.SWITCH_VIEWS_CHANGE_VIEW_POPUP,
                        action = {
                            issueNavigator.changeViewPopup()
                        }
                    )
                    meter.measure(
                        key = MeasureType.SWITCH_VIEWS_CHANGE_VIEW_TYPE,
                        action = {
                            issueNavigator.changeViewType(IssueNavigator.ViewType.DETAIL)
                            issueNavigator.waitForNavigator()
                        }
                    )
                } else if (viewType == IssueNavigator.ViewType.LIST) {
                    meter.measure(
                        key = MeasureType.SWITCH_VIEWS_CHANGE_VIEW_TYPE,
                        action = {
                            issueNavigator.changeViewType(IssueNavigator.ViewType.DETAIL)
                            issueNavigator.waitForNavigator()
                        }
                    )
                    meter.measure(
                        key = MeasureType.SWITCH_VIEWS_CHANGE_VIEW_POPUP,
                        action = {
                            issueNavigator.changeViewPopup()
                        }
                    )
                    meter.measure(
                        key = MeasureType.SWITCH_VIEWS_CHANGE_VIEW_TYPE,
                        action = {
                            issueNavigator.changeViewType(IssueNavigator.ViewType.LIST)
                            issueNavigator.waitForNavigator()
                        }
                    )
                } else {
                    Exception("Unrecognixed view type")
                }
            }
        )
    }

    private fun switchBetweenIssues() {
        issueNavigator
            .openNavigator()
            .waitForNavigator()

        meter.measure(
            key = SWITCH_BETWEEN_ISSUES_IN_SEARCH_RESULTS,
            action = {
                issueNavigator.selectIssue()
            }
        )
    }

    private fun openGlobalIssueSearch(): IssueNavigator {
        meter.measure(
            key = MeasureType.OPEN_GLOBAL_SEARCH,
            action = {
                jira.driver.navigate().to("/issues/?jql=order%20by%20lastViewed%20DESC%2C%20key%20DESC")
                issueNavigator.waitForNavigator()
            }
        )
        return issueNavigator
    }

    private fun customizeColumns() {
        jira.goToIssueNavigator("resolution = Unresolved ORDER BY priority DESC")
        meter.measure(
            key = MeasureType.CUSTOMIZE_COLUMNS,
            action = {
                columnsEditor.openEditor()
                columnsEditor.selectItems(2)
                columnsEditor.submitSelection()
            }
        )
    }

    private fun searchFilter() {
        val filter = filters.recall()!!
        meter.measure(
            key = SEARCH_WITH_JQL,
            action = {
                jira.navigateTo(filter.toString())
                issueNavigator.waitForNavigator()
            }
        )
    }

    private fun searchJcl() {
        val jqlQuery = jqlMemory.recall()!!
        meter.measure(
            key = SEARCH_WITH_JQL,
            action = fun(): IssueNavigatorPage {
                return jira.goToIssueNavigator(jqlQuery)
            },
            observation = fun(navigator: IssueNavigatorPage): JsonObject {
                val issueKeys = navigator.getIssueKeys()
                issueKeyMemory.remember(issueKeys)
                return SearchJqlObservation(
                    navigator.jql,
                    issueKeys.size,
                    -1 // work around https://ecosystem.atlassian.net/browse/JPERF-605
                ).serialize()
            }
        )
    }
}
