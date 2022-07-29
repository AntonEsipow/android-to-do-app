package com.bigtoapp.todo.ui.notes

import android.os.Bundle
import android.view.*
import com.airbnb.epoxy.EpoxyTouchHelper
import com.bigtoapp.todo.R
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.FragmentNotesBinding
import com.bigtoapp.todo.ui.BaseFragment
import com.bigtoapp.todo.ui.notes.bottomsheet.SortOrderBottomSheetDialogFragment

class NotesFragment: BaseFragment(), NoteEntityInterface {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        sharedViewModel.notesViewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            controller.viewState = viewState
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
                    sharedViewModel.deleteNote(noteThatWasRemoved.noteEntity)
                }
            })
    }

    override fun onItemSelected(noteEntity: NoteEntity) {
        val navDirections = NotesFragmentDirections
            .actionNotesFragmentToAddNoteEntityFragment(noteEntity.id)
        navigateViaNavGraph(navDirections)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == R.id.menuItemSort) {
            SortOrderBottomSheetDialogFragment().show(childFragmentManager, null)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}