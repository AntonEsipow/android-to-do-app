package com.bigtoapp.todo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_entity")
data class NoteEntity(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val description: String? = null,
    val priority: Int = 0,
    val createdAt: Long = 0L,
    val performDate: Long = 0L,
    val categoryId: String = ""
)