package com.bigtoapp.todo.ui.category

import com.bigtoapp.todo.database.entity.CategoryEntity

interface CategoryEntityInterface {

    fun onCategorySelected(categoryEntity: CategoryEntity)
}