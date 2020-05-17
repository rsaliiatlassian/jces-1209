package jces1209.vu.page

import jces1209.vu.page.boards.cloud.BoardPage
import jces1209.vu.page.boards.cloud.KanbanBoardPage
import jces1209.vu.page.boards.cloud.NextGenBoardPage
import jces1209.vu.page.boards.cloud.ScrumBoardPage
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.net.URI

class CloudBoardList(
    private val element: WebElement,
    private val driver: WebDriver
) {
    public val boardNameKanban = "Kanban"
    public val boardNameScrum = "Scrum"
    public val boardNameNextGen = "Next-gen"

    fun listBoards(): Map<String, Collection<BoardPage>> {
        val columnNames = element
            .findElements(By.tagName("th"))
            .map { it.text.trim() }
        val nameColumnIndex = columnNames.indexOf("Name")
        val typeColumnIndex = columnNames.indexOf("Type")
        val boards = mutableMapOf<String, MutableList<BoardPage>>()
        element
            .findElements(By.cssSelector("tbody tr"))
            .map { row -> row.findElements(By.tagName("td")) }
            .forEach { cells ->
                val type = cells[typeColumnIndex]!!.text.trim()
                val name = cells[nameColumnIndex]!!
                val uri = name.findElement(By.tagName("a")).getAttribute("href").let { URI(it) }
                val page = when (type) {
                    boardNameKanban -> KanbanBoardPage(driver, uri)
                    boardNameScrum -> ScrumBoardPage(driver, uri)
                    boardNameNextGen -> NextGenBoardPage(driver, uri)
                    else -> throw Exception("Unknown board type: $type")
                }
                if(null == boards[type]) {
                    boards[type] = mutableListOf()
                }
                boards[type]?.plusAssign(page)
            }
        return boards
    }
}
