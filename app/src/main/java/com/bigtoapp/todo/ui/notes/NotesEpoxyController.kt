package com.bigtoapp.todo.ui.notes

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.bigtoapp.todo.R
import com.bigtoapp.todo.addHeaderModel
import com.bigtoapp.todo.arch.notes.NotesViewState
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.ModelNoteEntityBinding
import com.bigtoapp.todo.ui.NoteEntityInterface
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel

class NotesEpoxyController(
    private val noteEntityInterface: NoteEntityInterface
): EpoxyController() {

    var viewState: NotesViewState = NotesViewState(isLoading = true)
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        if(viewState.isLoading) {
            // todo LoadingStateEpoxyModel
            return
        }

        if(viewState.dataList.isEmpty()) {
            // todo EmptyStateEpoxyModel
            return
        }

        viewState.dataList.forEach { dataItem ->
            if(dataItem.isHeader) {
                addHeaderModel(dataItem.data as String)
                return@forEach
            }

            val noteEntity = dataItem.data as NoteEntity
            NoteEntityEpoxyModel(noteEntity, noteEntityInterface)
                .id(noteEntity.id)
                .addTo(this)
        }
    }

    data class NoteEntityEpoxyModel(
        val noteEntity: NoteEntity,
        val noteEntityInterface: NoteEntityInterface
    ): ViewBindingKotlinModel<ModelNoteEntityBinding>(R.layout.model_note_entity) {

        override fun ModelNoteEntityBinding.bind() {

            titleTextView.text = noteEntity.title

            if(noteEntity.description == null) {
                descriptionTextView.isGone = true
            } else {
                descriptionTextView.isVisible = true
                descriptionTextView.text = noteEntity.description
            }

            categoryView.setOnClickListener {
                // todo call ChangeCategoryBottomSheet
            }

            categoryNameTextView.text = "Category"

            root.setOnClickListener {
                noteEntityInterface.onItemSelected(noteEntity)
            }
        }

    }
}