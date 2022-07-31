package com.bigtoapp.todo.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyTouchHelper
import com.bigtoapp.todo.Localizations
import com.bigtoapp.todo.R
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.FragmentCategoryBinding
import com.bigtoapp.todo.ui.BaseFragment
import com.bigtoapp.todo.ui.notes.NotesEpoxyController
import com.bigtoapp.todo.ui.notes.NotesFragmentDirections

class CategoryFragment: BaseFragment(), CategoryEntityInterface {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.title = Localizations.categoriesFragmentHeader

        hideKeyboard()
        binding.fab.setOnClickListener {
            navigateViaNavGraph(R.id.action_categoryFragment_to_addCategoryEntityFragment)
        }
        val controller = CategoryEpoxyController(this)
        binding.epoxyRecyclerView.setControllerAndBuildModels(controller)
        sharedViewModel.categoryEntitiesLiveData.observe(viewLifecycleOwner) { categoryEntityList ->
            controller.categoryEntityList = categoryEntityList as ArrayList<CategoryEntity>
        }

        // Setup swipe-to-delete
        EpoxyTouchHelper.initSwiping(binding.epoxyRecyclerView)
            .leftAndRight()
            .withTarget(CategoryEpoxyController.CategoryEntityEpoxyModel::class.java)
            .andCallbacks(object :
                EpoxyTouchHelper.SwipeCallbacks<CategoryEpoxyController.CategoryEntityEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: CategoryEpoxyController.CategoryEntityEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val categoryThatWasRemoved = model?.categoryEntity ?: return
                    sharedViewModel.deleteCategory(categoryThatWasRemoved)
                }
            })
    }

    override fun onCategorySelected(categoryEntity: CategoryEntity) {
        val navDirections = CategoryFragmentDirections
            .actionCategoryFragmentToAddCategoryEntityFragment(categoryEntity.id)
        navigateViaNavGraph(navDirections)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}