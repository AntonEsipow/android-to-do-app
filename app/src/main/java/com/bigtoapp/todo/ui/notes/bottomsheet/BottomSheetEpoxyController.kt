package com.bigtoapp.todo.ui.notes.bottomsheet

import android.graphics.Typeface
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.bigtoapp.todo.R
import com.bigtoapp.todo.arch.ToDoViewModel
import com.bigtoapp.todo.databinding.ModelSortOrderItemBinding
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel

class BottomSheetEpoxyController(
    private val selectedSort: ToDoViewModel.NotesViewState.Sort,
    private val sortOptions: Array<ToDoViewModel.NotesViewState.Sort>,
    private val selectedCallback: (ToDoViewModel.NotesViewState.Sort) -> Unit
): EpoxyController() {

    override fun buildModels() {
        sortOptions.forEach {
            val isSelected = it.name == selectedSort.name
            SortOrderItemEpoxyModel(isSelected, it, selectedCallback).id(it.displayName).addTo(this)
        }
    }

    data class SortOrderItemEpoxyModel(
        val isSelected: Boolean,
        val sort: ToDoViewModel.NotesViewState.Sort,
        val selectedCallback: (ToDoViewModel.NotesViewState.Sort) -> Unit
    ): ViewBindingKotlinModel<ModelSortOrderItemBinding>(R.layout.model_sort_order_item) {

        override fun ModelSortOrderItemBinding.bind() {
            textView.text = sort.displayName
            root.setOnClickListener { selectedCallback(sort) }

            // Styling for selected option
            textView.typeface = if(isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            view.visibility = if(isSelected) View.VISIBLE else View.GONE
        }
    }
}