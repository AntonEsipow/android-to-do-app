package com.bigtoapp.todo.ui.notes

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.bigtoapp.todo.Localizations
import com.bigtoapp.todo.R
import com.bigtoapp.todo.addHeaderModel
import com.bigtoapp.todo.arch.ToDoViewModel
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.database.entity.NoteWithCategoryEntity
import com.bigtoapp.todo.databinding.ModelNoteEntityBinding
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel
import com.bigtoapp.todo.ui.epoxy.models.EmptyStateEpoxyModel
import com.bigtoapp.todo.ui.epoxy.models.LoadingEpoxyModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotesEpoxyController(
    private val noteEntityInterface: NoteEntityInterface
): EpoxyController() {

    var viewState: ToDoViewModel.NotesViewState = ToDoViewModel.NotesViewState(isLoading = true)
        set(value) {
            field = value
            requestModelBuild()
        }
    override fun buildModels() {

        if(viewState.isLoading) {
            LoadingEpoxyModel().id("loading_state").addTo(this)
            return
        }

        if(viewState.dataList.isEmpty()) {
            emptyStateEpoxyModel()
            return
        }

        viewState.dataList.forEach{ dataItem ->
            if(dataItem.data.toString().isEmpty()) {
                emptyStateEpoxyModel()
            } else {
                if(dataItem.isHeader) {
                    addHeaderModel(dataItem.data as String)
                    return@forEach
                }
            }

            val noteWithCategoryEntity = dataItem.data as NoteWithCategoryEntity
            NoteEntityEpoxyModel(noteWithCategoryEntity, noteEntityInterface)
                .id(noteWithCategoryEntity.noteEntity.id)
                .addTo(this)
        }
    }

    private fun emptyStateEpoxyModel() {
        val title = Localizations.titleNoNotes
        val subtitle = Localizations.subtitleNoNotes
        EmptyStateEpoxyModel(title, subtitle).id("empty_state").addTo(this)
    }

    data class NoteEntityEpoxyModel(
        val noteEntity: NoteWithCategoryEntity,
        val noteEntityInterface: NoteEntityInterface
    ): ViewBindingKotlinModel<ModelNoteEntityBinding>(R.layout.model_note_entity) {

        override fun ModelNoteEntityBinding.bind() {

            titleTextView.text = noteEntity.noteEntity.title

            if(noteEntity.noteEntity.description == null) {
                descriptionTextView.isGone = true
            } else {
                descriptionTextView.isVisible = true
                descriptionTextView.text = noteEntity.noteEntity.description
            }

            categoryNameTextView.text = noteEntity.categoryEntity?.name

            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val performDate = dateFormatter.format(Date(noteEntity.noteEntity.performDate))
            val currentDate = dateFormatter.format(Date(System.currentTimeMillis()))
            performDateText.text = performDate
            if(performDateText.text == currentDate) {
                performDateText.setTextColor(ContextCompat.getColor(root.context, R.color.color_red))
            }

            val colorRes = R.color.white
            val color = noteEntity.categoryEntity?.color ?: ContextCompat.getColor(root.context, colorRes)
            categoryView.setBackgroundColor(color)
            root.setStrokeColor(ColorStateList.valueOf(color))

            root.setOnClickListener {
                noteEntityInterface.onItemSelected(noteEntity.noteEntity)
            }
        }

    }
}


