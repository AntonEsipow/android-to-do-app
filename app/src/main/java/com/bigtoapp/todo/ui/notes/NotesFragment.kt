package com.bigtoapp.todo.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            // todo navigateViaNAvGraph
        }

        val controller = NotesEpoxyController(this)
        binding.epoxyRecyclerView.setControllerAndBuildModels(controller)
        sharedViewModel.notesViewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            controller.viewState = viewState
        }
    }

    override fun onItemSelected(noteEntity: NoteEntity) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}