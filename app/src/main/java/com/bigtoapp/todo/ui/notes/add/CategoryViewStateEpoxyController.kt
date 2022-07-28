package com.bigtoapp.todo.ui.notes.add

import android.content.res.ColorStateList
import android.graphics.Typeface
import com.airbnb.epoxy.EpoxyController
import com.bigtoapp.todo.R
import com.bigtoapp.todo.arch.ToDoViewModel
import com.bigtoapp.todo.databinding.ModelCategoryEntityBinding
import com.bigtoapp.todo.databinding.ModelCategoryItemSelectionBinding
import com.bigtoapp.todo.getAttrColor
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel
import com.bigtoapp.todo.ui.epoxy.models.LoadingEpoxyModel

class CategoryViewStateEpoxyController(
    private val onCategorySelected: (String) -> Unit
): EpoxyController() {

    var viewState = ToDoViewModel.CategoriesViewState()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        if (viewState.isLoading) {
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        viewState.itemList.forEach { item ->
            CategoryViewStateItem(item, onCategorySelected).id(item.categoryEntity.id).addTo(this)
        }
    }

    data class CategoryViewStateItem(
        val item: ToDoViewModel.CategoriesViewState.Item,
        private val onCategorySelected: (String) -> Unit
    ): ViewBindingKotlinModel<ModelCategoryItemSelectionBinding>(R.layout.model_category_item_selection) {

        override fun ModelCategoryItemSelectionBinding.bind() {

            textView.text = item.categoryEntity.name
            categoryView.setBackgroundColor(item.categoryEntity.color)
            root.setOnClickListener { onCategorySelected(item.categoryEntity.id) }

            val colorRes = if (item.isSelected) R.attr.colorSecondary else R.attr.colorOnSurface
            val color = root.getAttrColor(colorRes)
            textView.setTextColor(color)
            textView.typeface = if (item.isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            root.setStrokeColor(ColorStateList.valueOf(color))
        }
    }
}