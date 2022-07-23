package com.bigtoapp.todo.arch

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigtoapp.todo.database.AppDatabase
import com.bigtoapp.todo.database.entity.NoteEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class ToDoViewModel: ViewModel() {

    private lateinit var repository: ToDoRepository

    private val _noteEntitiesLiveData = MutableLiveData<List<NoteEntity>>()
    val noteEntitiesLiveData: LiveData<List<NoteEntity>>
        get() = _noteEntitiesLiveData

    private val _transactionCompletedLiveData = MutableLiveData<Event<Boolean>>()
    val transactionCompletedLiveData: LiveData<Event<Boolean>>
        get() = _transactionCompletedLiveData

    fun init(appDatabase: AppDatabase) {
        repository = ToDoRepository(appDatabase)

        // Initialize our Flow connectivity to the DB
        viewModelScope.launch {
            repository.getAllNotes().collect { notes ->
                _noteEntitiesLiveData.postValue(notes)
            }
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
}