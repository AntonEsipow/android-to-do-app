package com.bigtoapp.todo.database.dao

import androidx.room.*
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.database.entity.NoteWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteEntityDao {

    @Query("SELECT * FROM note_entity")
    fun getAllNoteEntities(): Flow<List<NoteEntity>>

    @Transaction
    @Query("SELECT * FROM note_entity")
    fun getAllNotesWithCategoryEntities(): Flow<List<NoteWithCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg noteEntity: NoteEntity)

    @Delete
    suspend fun delete(noteEntity: NoteEntity)

    @Update
    suspend fun update(noteEntity: NoteEntity)
}