package com.bigtoapp.todo.ui.category

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyController
import com.bigtoapp.todo.R
import com.bigtoapp.todo.addHeaderModel
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.ModelCategoryEntityBinding
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel
import com.bigtoapp.todo.ui.epoxy.models.EmptyStateEpoxyModel
import com.bigtoapp.todo.ui.epoxy.models.LoadingEpoxyModel
import com.bigtoapp.todo.ui.notes.NotesEpoxyController

class CategoryEpoxyController(
    private val categoryEntityInterface: CategoryEntityInterface
): EpoxyController() {

    private var isLoading: Boolean = true
        set(value) {
            field = value
            if(field) {
                requestModelBuild()
            }
        }

    var categoryEntityList = ArrayList<CategoryEntity>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {

        if(isLoading) {
            LoadingEpoxyModel().id("loading_state").addTo(this)
            return
        }

        if(categoryEntityList.isEmpty()) {
            EmptyStateEpoxyModel().id("empty_state").addTo(this)
            return
        }

        addHeaderModel("All categories")
        categoryEntityList.forEach { category ->
            CategoryEpoxyController
                .CategoryEntityEpoxyModel(category, categoryEntityInterface).id(category.id).addTo(this)
        }
    }

    data class CategoryEntityEpoxyModel(
        val categoryEntity: CategoryEntity,
        val categoryEntityInterface: CategoryEntityInterface
    ): ViewBindingKotlinModel<ModelCategoryEntityBinding>(R.layout.model_category_entity) {

        override fun ModelCategoryEntityBinding.bind() {

            nameCategoryText.text = categoryEntity.name

            val colorRes = R.color.teal_700
            val color = ContextCompat.getColor(root.context, colorRes)
            categoryView.setBackgroundColor(color)
            root.setStrokeColor(ColorStateList.valueOf(color))

            root.setOnClickListener {
                categoryEntityInterface.onCategorySelected(categoryEntity)
            }
        }

    }
}