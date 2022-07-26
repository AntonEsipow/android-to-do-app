package com.bigtoapp.todo.arch

import com.bigtoapp.todo.database.AppDatabase
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import java.lang.Appendable

class ToDoRepository(
    private val appDatabase: AppDatabase
) {
    // region NoteEntity
    suspend fun insertNote(noteEntity: NoteEntity) {
        appDatabase.noteEntityDao().insert(noteEntity)
    }

    suspend fun deleteNote(noteEntity: NoteEntity) {
        appDatabase.noteEntityDao().delete(noteEntity)
    }

    suspend fun updateNote(noteEntity: NoteEntity) {
        appDatabase.noteEntityDao().update(noteEntity)
    }

    fun getAllNotes(): Flow<List<NoteEntity>> {
        return appDatabase.noteEntityDao().getAllNoteEntities()
    }
    // endregion NoteEntity

    // region CategoryEntity
    suspend fun insertCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().insert(categoryEntity)
    }

    suspend fun deleteCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().delete(categoryEntity)
    }

    suspend fun updateCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().update(categoryEntity)
    }

    fun getAllCategories(): Flow<List<CategoryEntity>> {
        return appDatabase.categoryEntityDao().getAllCategoryEntities()
    }
    // endregion CategoryEntity
}