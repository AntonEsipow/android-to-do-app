package com.bigtoapp.todo.ui.notes.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bigtoapp.todo.arch.ToDoViewModel
import com.bigtoapp.todo.databinding.BottomSheetSortOrderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortOrderBottomSheetDialogFragment: BottomSheetDialogFragment() {

    private var _binding: BottomSheetSortOrderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ToDoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetSortOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val epoxyController = BottomSheetEpoxyController(
            viewModel.currentSort,
            ToDoViewModel.NotesViewState.Sort.values()) {
            viewModel.currentSort = it
            dismiss()
        }
        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}