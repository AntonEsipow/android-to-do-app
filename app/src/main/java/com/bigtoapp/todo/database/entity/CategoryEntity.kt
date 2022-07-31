package com.bigtoapp.todo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bigtoapp.todo.Localizations

@Entity(tableName = "category_entity")
data class CategoryEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val color: Int = 0
){
    companion object {
        const val DEFAULT_CATEGORY_ID = ""

        fun getDefaultCategory(): CategoryEntity {
            return CategoryEntity(DEFAULT_CATEGORY_ID, Localizations.defCategoryName, 0)
        }
    }
}