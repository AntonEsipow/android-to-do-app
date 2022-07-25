package com.bigtoapp.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bigtoapp.todo.database.dao.CategoryEntityDao
import com.bigtoapp.todo.database.dao.NoteEntityDao
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.database.entity.NoteEntity

@Database(
    entities = [NoteEntity::class, CategoryEntity::class],
    version = 2
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
                .addMigrations(MIGRATION_1_2())
                .build()
            return appDatabase!!
        }
    }

    class MIGRATION_1_2: Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `category_entity` " +
                    "(`id` TEXT NOT NULL, `name` TEXT NOT NULL, `color` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        }

    }

    abstract fun noteEntityDao(): NoteEntityDao
    abstract fun categoryEntityDao(): CategoryEntityDao
}