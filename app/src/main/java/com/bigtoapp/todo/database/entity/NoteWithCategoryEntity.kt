package com.bigtoapp.todo.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithCategoryEntity(

    @Embedded
    val noteEntity: NoteEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val categoryEntity: CategoryEntity?
)