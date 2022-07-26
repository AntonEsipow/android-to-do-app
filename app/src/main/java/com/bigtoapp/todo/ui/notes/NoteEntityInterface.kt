package com.bigtoapp.todo.ui.notes

import com.bigtoapp.todo.database.entity.NoteEntity

interface NoteEntityInterface {

    fun onItemSelected(noteEntity: NoteEntity)
}