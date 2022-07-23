package com.bigtoapp.todo.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyTouchHelper
import com.bigtoapp.todo.R
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.FragmentNotesBinding
import com.bigtoapp.todo.ui.BaseFragment
import com.bigtoapp.todo.ui.NoteEntityInterface

class NotesFragment: BaseFragment(), NoteEntityInterface {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideKeyboard()
        binding.fab.setOnClickListener {
            navigateViaNavGraph(R.id.action_notesFragment_to_addNoteEntityFragment)
        }

        val controller = NotesEpoxyController(this)
        binding.epoxyRecyclerView.setControllerAndBuildModels(controller)
        sharedViewModel.noteEntitiesLiveData.observe(viewLifecycleOwner) { noteEntityList ->
            controller.noteEntityList = noteEntityList as ArrayList<NoteEntity>
        }

        // Setup swipe-to-delete
        EpoxyTouchHelper.initSwiping(binding.epoxyRecyclerView)
            .leftAndRight()
            .withTarget(NotesEpoxyController.NoteEntityEpoxyModel::class.java)
            .andCallbacks(object :
                EpoxyTouchHelper.SwipeCallbacks<NotesEpoxyController.NoteEntityEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: NotesEpoxyController.NoteEntityEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val noteThatWasRemoved = model?.noteEntity ?: return
                    sharedViewModel.deleteNote(noteThatWasRemoved)
                }
            })
    }

    override fun onItemSelected(noteEntity: NoteEntity) {
        val navDirections = NotesFragmentDirections
            .actionNotesFragmentToAddNoteEntityFragment(noteEntity.id)
        navigateViaNavGraph(navDirections)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}