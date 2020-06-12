package jces1209.vu.page.boards.browse.cloud

import jces1209.vu.page.boards.browse.BoardList
import jces1209.vu.page.boards.view.cloud.CloudKanbanBoardPage
import jces1209.vu.page.boards.view.cloud.CloudNextGenBoardPage
import jces1209.vu.page.boards.view.cloud.CloudScrumBacklogPage
import jces1209.vu.page.boards.view.cloud.CloudScrumSprintPage
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.net.URI

class CloudBoardList(
    private val element: WebElement,
    private val driver: WebDriver
) : BoardList() {

    override fun listBoards(): MixedBoards {
        val columnNames = element
            .findElements(By.tagName("th"))
            .map { it.text.trim() }
        val nameColumnIndex = columnNames.indexOf("Name")
        val typeColumnIndex = columnNames.indexOf("Type")
        val kanbanBoards = mutableListOf<CloudKanbanBoardPage>()
        val backlogBoards = mutableListOf<CloudScrumBacklogPage>()
        val sprintBoards = mutableListOf<CloudScrumSprintPage>()
        val nextGenBoards = mutableListOf<CloudNextGenBoardPage>()
        element
            .findElements(By.cssSelector("tbody tr"))
            .map { row -> row.findElements(By.tagName("td")) }
            .forEach { cells ->
                val type = cells[typeColumnIndex]!!.text.trim()
                val name = cells[nameColumnIndex]!!
                val uri = name.findElement(By.tagName("a")).getAttribute("href").let { it.removeSuffix("&useStoredSettings=true") }
                when (type) {
                    "Kanban" -> kanbanBoards.add(CloudKanbanBoardPage(driver, URI(uri)))
                    "Scrum" -> {
                        backlogBoards.add(CloudScrumBacklogPage(driver, URI("$uri&view=planning&issueLimit=100")))
                        sprintBoards.add(CloudScrumSprintPage(driver, URI(uri)))
                    }
                    "Next-gen" -> nextGenBoards.add(CloudNextGenBoardPage(driver, URI(uri)))
                    else -> throw Exception("Unknown board type: $type")
                }
            }
        return MixedBoards(kanbanBoards, backlogBoards, sprintBoards, nextGenBoards)
    }
}
