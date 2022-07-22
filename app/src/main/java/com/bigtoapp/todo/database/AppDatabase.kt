package com.bigtoapp.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bigtoapp.todo.database.dao.NoteEntityDao
import com.bigtoapp.todo.database.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        private var appDatabase: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if(appDatabase != null) {
                return appDatabase!!
            }

            appDatabase = Room
                .databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "to-do-database"
                )
                .build()
            return appDatabase!!
        }
    }

    abstract fun noteEntityDao(): NoteEntityDao
}