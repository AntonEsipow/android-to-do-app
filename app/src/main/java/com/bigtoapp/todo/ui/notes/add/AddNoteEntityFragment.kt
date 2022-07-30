package com.bigtoapp.todo.ui.notes.add

import android.graphics.Color
import android.os.Binder
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.EpoxyRecyclerView
import com.bigtoapp.todo.R
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.database.entity.NoteWithCategoryEntity
import com.bigtoapp.todo.databinding.FragmentAddNoteBinding
import com.bigtoapp.todo.ui.BaseFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class AddNoteEntityFragment: BaseFragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: AddNoteEntityFragmentArgs by navArgs()
    private val selectedNoteWithCategoryEntity: NoteWithCategoryEntity? by lazy {
        sharedViewModel.noteWithCategoryEntityLiveData.value?.find {
            it.noteEntity.id == safeArgs.selectedItemEntityId
        }
    }
    private val categoryViewStateEpoxyController = CategoryViewStateEpoxyController { categoryId ->
        sharedViewModel.onCategorySelected(categoryId)
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
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDate = System.currentTimeMillis()
        binding.performedDateTextView.text = dateFormatter(currentDate)
        binding.categoryNameTextView.text = CategoryEntity.getDefaultCategory().name

        showKeyboard()
        sharedViewModel.transactionCompletedLiveData.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
                if (isInEditMode) {
                    Toast.makeText(requireActivity(), "Note successfully updated!", Toast.LENGTH_SHORT).show()
                    navigateUp()
                    return@observe
                }
                refreshAddItemEntityScreen()
            }
        }
        binding.titleEditText.requestFocus()
        // Setup screen if we are in edit mode
        setupSelectedItemEntity()
        sharedViewModel.onCategorySelected(selectedNoteWithCategoryEntity?.noteEntity?.categoryId ?: CategoryEntity.DEFAULT_CATEGORY_ID, true)
        sharedViewModel.categoriesViewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            categoryViewStateEpoxyController.viewState = viewState
        }
    }

    private fun saveNoteEntityToTheDatabase() {
        val noteTitle = binding.titleEditText.text.toString().trim()
        if(noteTitle.isEmpty()) {
            binding.titleEditText.error = "* Required field"
            return
        } else {
            binding.titleEditText.error = null
        }
        val itemCategoryId = sharedViewModel.categoriesViewStateLiveData.value?.getSelectedCategoryId() ?: return

        var noteDescription: String? = binding.descriptionEditText.text.toString().trim()
        if(noteDescription?.isEmpty() == true) {
            noteDescription = null
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = binding.performedDateTextView.text.toString()
        val notePerformDate: Long = dateFormat.parse(date).time

        if(isInEditMode) {
            val noteEntity = selectedNoteWithCategoryEntity!!.noteEntity.copy(
                title = noteTitle,
                description = noteDescription,
                performDate = notePerformDate,
                categoryId = itemCategoryId
            )
            sharedViewModel.updateNote(noteEntity)
            return
        }

        val noteEntity = NoteEntity(
            id = UUID.randomUUID().toString(),
            title = noteTitle,
            description = noteDescription,
            performDate = notePerformDate,
            createdAt = System.currentTimeMillis(),
            categoryId = itemCategoryId
        )
        sharedViewModel.insertNote(noteEntity)
    }

    private fun refreshAddItemEntityScreen() {
        binding.titleEditText.text = null
        binding.titleEditText.requestFocus()
        binding.descriptionEditText.text = null
        binding.performedDateTextView.text = dateFormatter(System.currentTimeMillis())
        binding.categoryNameTextView.text = CategoryEntity.getDefaultCategory().name
        Toast.makeText(requireActivity(), "Note successfully added!", Toast.LENGTH_SHORT).show()
    }

    private fun setupSelectedItemEntity() {
        selectedNoteWithCategoryEntity?.let { noteWithCategoryEntity ->
            isInEditMode = true

            binding.titleEditText.setText(noteWithCategoryEntity.noteEntity.title)
            binding.titleEditText.setSelection(noteWithCategoryEntity.noteEntity.title.length)
            binding.descriptionEditText.setText(noteWithCategoryEntity.noteEntity.description)
            binding.performedDateTextView.setText(dateFormatter(noteWithCategoryEntity.noteEntity.performDate))
            binding.categoryNameTextView.setText(noteWithCategoryEntity.categoryEntity?.name ?: CategoryEntity.getDefaultCategory().name)
            mainActivity.supportActionBar?.title = "Update note"
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menuItemAdd -> {
                onMenuAddClicked()
                true
            }
            R.id.menuEditPerformDate -> {
                onPickDate()
                true
            }
            R.id.menuEditCategory -> {
                onSelectCategoryPicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onMenuAddClicked() {
        saveNoteEntityToTheDatabase()
    }

    private fun onPickDate(){
        hideKeyboard()
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select perform date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
        // Setting up the event for when ok is clicked
        datePicker.addOnPositiveButtonClickListener {
            // formatting date in dd-mm-yyyy format.
            val date = dateFormatter(it)
            binding.performedDateTextView.text = date
            Toast.makeText(requireActivity(), "$date is selected", Toast.LENGTH_LONG).show()
        }
        // Setting up the event for when cancelled is clicked
        datePicker.addOnNegativeButtonClickListener {
            Toast.makeText(requireActivity(), "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
        }
        // Setting up the event for when back button is pressed
        datePicker.addOnCancelListener {
            // No message
        }
    }

    private fun dateFormatter(date: Long): String {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        return dateFormatter.format(Date(date))
    }

    private fun onSelectCategoryPicked() {
        val selectCategoryDialog = LayoutInflater.from(requireActivity())
            .inflate(R.layout.dialog_select_category,null,false)
        val categoriesRecyclerView = selectCategoryDialog.findViewById<EpoxyRecyclerView>(R.id.categoriesEpoxyRecyclerView)
        categoriesRecyclerView.setController(categoryViewStateEpoxyController)
        MaterialAlertDialogBuilder(requireActivity())
            .setView(selectCategoryDialog)
            .setTitle("Select Category")
            .setPositiveButton("Select") { dialog, _ ->
                binding.categoryNameTextView.text = sharedViewModel.categoriesViewStateLiveData.value?.getSelectedCategoryName()
                Toast.makeText(requireActivity(), "Category selected!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                binding.categoryNameTextView.text = sharedViewModel.categoriesViewStateLiveData.value?.getSelectedCategoryName()
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}