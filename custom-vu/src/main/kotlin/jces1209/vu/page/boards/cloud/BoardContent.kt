package jces1209.vu.page.boards.cloud

interface BoardContent {

    fun getIssueCount(): Int
    fun getIssueKeys(): Collection<String>
}
