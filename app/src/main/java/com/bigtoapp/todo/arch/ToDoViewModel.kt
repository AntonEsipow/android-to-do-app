package com.bigtoapp.todo.arch

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigtoapp.todo.arch.notes.NotesViewState
import com.bigtoapp.todo.database.AppDatabase
import com.bigtoapp.todo.database.entity.NoteEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class ToDoViewModel: ViewModel() {

    private lateinit var repository: ToDoRepository

    private val _noteEntitiesLiveData = MutableLiveData<List<NoteEntity>>()
    val noteEntitiesLiveData: LiveData<List<NoteEntity>> get() = _noteEntitiesLiveData

    // region Home page
    var currentSort: NotesViewState.Sort = NotesViewState.Sort.NEWEST
        set(value) {
            field = value
            updateNotesViewState(_noteEntitiesLiveData.value!!)
        }

    private val _notesViewStateLiveData = MutableLiveData<NotesViewState>()
    val notesViewStateLiveData: LiveData<NotesViewState> get() = _notesViewStateLiveData
    // endregion Home page

    fun init(appDatabase: AppDatabase) {
        repository = ToDoRepository(appDatabase)

        // Initialize our Flow connectivity to the DB
        viewModelScope.launch {
            repository.getAllNotes().collect { notes ->
                _noteEntitiesLiveData.postValue(notes)

                updateNotesViewState(notes)
            }
        }
    }

    private fun updateNotesViewState(notes: List<NoteEntity>) {

        val dataList = ArrayList<NotesViewState.DataItem<*>>()

        when(currentSort){
            NotesViewState.Sort.NEWEST -> sortByNewest(notes, dataList)
        }
    }

    private fun sortByNewest(items: List<NoteEntity>,dataList: ArrayList<NotesViewState.DataItem<*>>) {
        val headerItem = NotesViewState.DataItem(
            data = "Newest",
            isHeader = true
        )
        dataList.add(headerItem)
        items.sortedByDescending {
            it.createdAt
        }.forEach { item ->
            val dataItem = NotesViewState.DataItem(data = item)
            dataList.add(dataItem)
        }
    }

    // region NoteEntity
    fun insertNote(noteEntity: NoteEntity){
        viewModelScope.launch {
            repository.insertNote(noteEntity)
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
        }
    }
    // endregion NoteEntity
}