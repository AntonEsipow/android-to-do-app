package com.bigtoapp.todo.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bigtoapp.todo.databinding.FragmentNotesBinding
import com.bigtoapp.todo.ui.BaseFragment

class NotesFragment: BaseFragment() {

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

        sharedViewModel.noteEntitiesLiveData.observe(viewLifecycleOwner) { noteEntityList ->
            // todo
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}