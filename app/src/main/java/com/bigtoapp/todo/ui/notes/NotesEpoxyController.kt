package com.bigtoapp.todo.ui.notes

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.bigtoapp.todo.R
import com.bigtoapp.todo.addHeaderModel
import com.bigtoapp.todo.database.entity.NoteEntity
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

    private var isLoading: Boolean = true
        set(value) {
            field = value
            if(field) {
                requestModelBuild()
            }
        }

    var noteEntityList = ArrayList<NoteEntity>()
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

        if(noteEntityList.isEmpty()) {
            EmptyStateEpoxyModel().id("empty_state").addTo(this)
            return
        }

        addHeaderModel("Newest")
        noteEntityList.forEach { note ->
            NoteEntityEpoxyModel(note, noteEntityInterface).id(note.id).addTo(this)
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

            categoryNameTextView.text = "Category"

            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val performDate = dateFormatter.format(Date(noteEntity.performDate))
            val currentDate = dateFormatter.format(Date(System.currentTimeMillis()))
            performDateText.text = performDate
            if(performDateText.text == currentDate) {
                performDateText.setTextColor(ContextCompat.getColor(root.context, R.color.red))
            }

            val colorRes = R.color.teal_700
            val color = ContextCompat.getColor(root.context, colorRes)
            categoryView.setBackgroundColor(color)
            root.setStrokeColor(ColorStateList.valueOf(color))

            root.setOnClickListener {
                noteEntityInterface.onItemSelected(noteEntity)
            }
        }

    }
}


