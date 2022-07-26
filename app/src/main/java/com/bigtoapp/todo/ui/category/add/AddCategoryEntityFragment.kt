package com.bigtoapp.todo.ui.category.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bigtoapp.todo.R
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.FragmentAddCategoryBinding
import com.bigtoapp.todo.ui.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class AddCategoryEntityFragment: BaseFragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: AddCategoryEntityFragmentArgs by navArgs()
    private val selectedCategoryEntity: CategoryEntity? by lazy {
        sharedViewModel.categoryEntitiesLiveData.value?.find {
            it.id == safeArgs.selectedCategoryEntityId
        }
    }

    private var isInEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showKeyboard()
        sharedViewModel.transactionCompletedLiveData.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
                navigateUp()
                return@observe
            }
        }
        // Setup screen if we are in edit mode
        binding.categoryNameText.requestFocus()
        setupSelectedCategoryEntity()
    }

    private fun saveCategoryEntityToTheDatabase() {
        val categoryName = binding.categoryNameText.text.toString().trim()
        if(categoryName.isEmpty()) {
            binding.categoryNameText.error = "* Required field"
            return
        } else {
            binding.categoryNameText.error = null
        }

        if(isInEditMode) {
            val categoryEntity = selectedCategoryEntity!!.copy(
                name = categoryName
            )
            sharedViewModel.updateCategory(categoryEntity)
            return
        }

        val categoryEntity = CategoryEntity(
            id = UUID.randomUUID().toString(),
            name = categoryName
        )
        sharedViewModel.insertCategory(categoryEntity)
        Toast.makeText(requireActivity(), "Category successfully added!", Toast.LENGTH_SHORT).show()
    }

    private fun setupSelectedCategoryEntity() {
        selectedCategoryEntity?.let { categoryEntity ->
            isInEditMode = true

            binding.categoryNameText.setText(categoryEntity.name)
            mainActivity.supportActionBar?.title = "Update category"
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_category, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menuCategoryAdd -> {
                onMenuAddClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onMenuAddClicked() {
        saveCategoryEntityToTheDatabase()
        if(isInEditMode) {
            Toast.makeText(requireActivity(), "Updated!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "Added!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}