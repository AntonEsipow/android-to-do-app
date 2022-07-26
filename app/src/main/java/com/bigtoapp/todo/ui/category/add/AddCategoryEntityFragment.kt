package com.bigtoapp.todo.ui.category.add

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bigtoapp.todo.R
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.FragmentAddCategoryBinding
import com.bigtoapp.todo.ui.BaseFragment
import com.bigtoapp.todo.ui.category.color.CustomColorPickerViewModel
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
    private val viewModel: CustomColorPickerViewModel by viewModels()

    private var isInEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private class SeekBarListener(
        private val onProgressChange: (Int) -> Unit
    ) : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onProgressChange(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // Nothing to do
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // Nothing to do
        }
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

        if(isInEditMode) {
            viewModel.setCategoryColorName(selectedCategoryEntity!!.color, selectedCategoryEntity!!.name) { red, green, blue ->
                binding.customColorPicker.redColorLayout.seekBar.progress = red
                binding.customColorPicker.greenColorLayout.seekBar.progress = green
                binding.customColorPicker.blueColorLayout.seekBar.progress = blue
            }
        }

        binding.customColorPicker.redColorLayout.apply {
            colorSliderTextView.text = "Red"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onRedChange))
        }

        binding.customColorPicker.greenColorLayout.apply {
            colorSliderTextView.text = "Green"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onGreenChange))
        }

        binding.customColorPicker.blueColorLayout.apply {
            colorSliderTextView.text = "Blue"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onBlueChange))
        }

        viewModel.colorViewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            binding.customColorPicker.titleTextView.text = viewState.getFormattedTitle()

            val color = Color.rgb(viewState.red, viewState.green, viewState.blue)
            binding.customColorPicker.colorView.setBackgroundColor(color)
        }
    }

    private fun saveCategoryEntityToTheDatabase() {
        val categoryName = binding.categoryNameText.text.toString().trim()
        if(categoryName.isEmpty()) {
            binding.categoryNameText.error = "* Required field"
            return
        } else {
            binding.categoryNameText.error = null
        }

        val color = setColorIfIsNull()

        if(isInEditMode) {
            val categoryEntity = selectedCategoryEntity!!.copy(
                name = categoryName,
                color = color
            )
            sharedViewModel.updateCategory(categoryEntity)
            return
        }

        val categoryEntity = CategoryEntity(
            id = UUID.randomUUID().toString(),
            name = categoryName,
            color = color
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

    private fun setColorIfIsNull(): Int {
        return when(val viewState = viewModel.colorViewStateLiveData.value) {
            null -> Color.rgb(243, 243, 243)
            else -> Color.rgb(viewState.red, viewState.green, viewState.blue)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}