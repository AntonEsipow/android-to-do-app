package com.bigtoapp.todo.ui

import com.bigtoapp.todo.database.entity.NoteEntity

interface NoteEntityInterface {

    fun onItemSelected(noteEntity: NoteEntity)
}