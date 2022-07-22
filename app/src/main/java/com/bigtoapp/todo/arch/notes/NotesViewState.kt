package com.bigtoapp.todo.arch.notes

data class NotesViewState(
    val dataList: List<DataItem<*>> = emptyList(),
    val isLoading: Boolean = false,
    val sort: Sort = Sort.NEWEST
) {
    data class DataItem<T>(
        val data: T,
        val isHeader: Boolean = false
    )

    enum class Sort(val displayName: String) {
        NEWEST("Newest")
    }
}