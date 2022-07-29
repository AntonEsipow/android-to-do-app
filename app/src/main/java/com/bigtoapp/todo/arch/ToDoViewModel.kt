package com.bigtoapp.todo.arch

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigtoapp.todo.database.AppDatabase
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.database.entity.NoteWithCategoryEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ToDoViewModel: ViewModel() {

    private lateinit var repository: ToDoRepository

    private val _noteEntitiesLiveData = MutableLiveData<List<NoteEntity>>()
    val noteEntitiesLiveData: LiveData<List<NoteEntity>>
        get() = _noteEntitiesLiveData

    private val _noteWithCategoryEntityLiveData = MutableLiveData<List<NoteWithCategoryEntity>>()
    val noteWithCategoryEntityLiveData: LiveData<List<NoteWithCategoryEntity>>
        get() = _noteWithCategoryEntityLiveData

    private val _transactionCompletedLiveData = MutableLiveData<Event<Boolean>>()
    val transactionCompletedLiveData: LiveData<Event<Boolean>>
        get() = _transactionCompletedLiveData

    // Categories
    private val _categoriesViewStateLiveData = MutableLiveData<CategoriesViewState>()
    val categoriesViewStateLiveData: LiveData<CategoriesViewState>
        get() = _categoriesViewStateLiveData

    private val _categoryEntitiesLiveData = MutableLiveData<List<CategoryEntity>>()
    val categoryEntitiesLiveData: LiveData<List<CategoryEntity>>
        get() = _categoryEntitiesLiveData

    // Notes
    private val dataList = ArrayList<NotesViewState.DataItem<*>>()
    private val currentSort: NotesViewState.Sort = NotesViewState.Sort.CURRENT
    private val _notesViewStateLiveData = MutableLiveData<NotesViewState>()
    val notesViewStateLiveData: LiveData<NotesViewState>
        get() = _notesViewStateLiveData


    fun init(appDatabase: AppDatabase) {
        repository = ToDoRepository(appDatabase)

        // Initialize our Flow connectivity to the DB
        viewModelScope.launch {
            repository.getAllNotes().collect { notes ->
                _noteEntitiesLiveData.postValue(notes)
            }
        }

        viewModelScope.launch {
            repository.getAllNotesWithCategoryEntities().collect{ notes ->
                _noteWithCategoryEntityLiveData.postValue(notes)

                updateHomeViewState(notes)
            }
        }

        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                _categoryEntitiesLiveData.postValue(categories)
            }
        }
    }

    private fun updateHomeViewState(notes: List<NoteWithCategoryEntity>) {

        when(currentSort) {
            NotesViewState.Sort.CURRENT -> sortingByCurrent(notes, dataList)
            NotesViewState.Sort.CATEGORY -> {
                // todo
            }
            NotesViewState.Sort.ALL -> {
                // todo
            }
            NotesViewState.Sort.PERFORM -> {
                // todo
            }
        }

        _notesViewStateLiveData.postValue(
            NotesViewState(
                dataList = dataList,
                isLoading = false,
                sort = currentSort
            )
        )
    }

    private fun sortingByCurrent(notes: List<NoteWithCategoryEntity>, dataList: ArrayList<NotesViewState.DataItem<*>>) {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = dateFormatter.format(Date(System.currentTimeMillis()))
        val headerItem = NotesViewState.DataItem(
            data = "Current date",
            isHeader = true
        )
        dataList.add(headerItem)
        notes.sortedBy {
            it.noteEntity.performDate
        }.forEach { note ->
            val performDate = dateFormatter.format(Date(note.noteEntity.performDate))
            if(currentDate == performDate) {
                val dataItem = NotesViewState.DataItem( data = note)
                dataList.add(dataItem)
            }
        }
    }

    data class NotesViewState(
        val dataList: List<DataItem<*>> = emptyList(),
        val isLoading: Boolean = false,
        val sort: Sort = Sort.CURRENT
    ) {
        data class DataItem<T>(
            val data: T,
            val isHeader: Boolean = false
        )

        enum class Sort(val displayName: String) {
            CATEGORY("Category"),
            PERFORM("Perform date"),
            CURRENT("Current date"),
            ALL("All notes")
        }
    }

    fun onCategorySelected(categoryId: String, showLoading: Boolean = false) {
        if (showLoading) {
            val loadingViewState = CategoriesViewState(isLoading = true)
            _categoriesViewStateLiveData.value = loadingViewState
        }

        val categories = categoryEntitiesLiveData.value ?: return
        val viewStateItemList = ArrayList<CategoriesViewState.Item>()

        // Default category (un-selecting a category)
        viewStateItemList.add(CategoriesViewState.Item(
            categoryEntity = CategoryEntity.getDefaultCategory(),
            isSelected = categoryId == CategoryEntity.DEFAULT_CATEGORY_ID
        ))

        // Populate the rest of the list with what we have in the DB
        categories.forEach {
            viewStateItemList.add(CategoriesViewState.Item(
                categoryEntity = it,
                isSelected = it.id == categoryId
            ))
        }
        val viewState = CategoriesViewState(itemList = viewStateItemList)
        _categoriesViewStateLiveData.postValue(viewState)
    }

    data class CategoriesViewState(
        val isLoading: Boolean = false,
        val itemList: List<Item> = emptyList()
    ) {
        data class Item(
            val categoryEntity: CategoryEntity = CategoryEntity(),
            val isSelected: Boolean = false
        )

        fun getSelectedCategoryId(): String {
            return itemList.find { it.isSelected }?.categoryEntity?.id ?: CategoryEntity.DEFAULT_CATEGORY_ID
        }
        fun getSelectedCategoryName(): String {
            return itemList.find { it.isSelected }?.categoryEntity?.name ?: CategoryEntity.getDefaultCategory().name
        }
    }

    // region NoteEntity
    fun insertNote(noteEntity: NoteEntity){
        viewModelScope.launch {
            repository.insertNote(noteEntity)

            _transactionCompletedLiveData.postValue(Event(true))
        }
    }

    fun deleteNote(noteEntity: NoteEntity){
        viewModelScope.launch {
            repository.deleteNote(noteEntity)
        }
    }

    fun updateNote(noteEntity: NoteEntity){
        viewModelScope.launch {
            repository.updateNote(noteEntity)

            _transactionCompletedLiveData.postValue(Event(true))
        }
    }
    // endregion NoteEntity

    //region CategoryEntity
    fun insertCategory(categoryEntity: CategoryEntity){
        viewModelScope.launch {
            repository.insertCategory(categoryEntity)

            _transactionCompletedLiveData.postValue(Event(true))
        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity){
        viewModelScope.launch {
            repository.deleteCategory(categoryEntity)
        }
    }

    fun updateCategory(categoryEntity: CategoryEntity){
        viewModelScope.launch {
            repository.updateCategory(categoryEntity)

            _transactionCompletedLiveData.postValue(Event(true))
        }
    }
    // endregion CategoryEntity
}