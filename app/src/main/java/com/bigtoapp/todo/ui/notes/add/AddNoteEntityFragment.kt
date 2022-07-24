package com.bigtoapp.todo.ui.notes.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bigtoapp.todo.R
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.FragmentAddNoteBinding
import com.bigtoapp.todo.ui.BaseFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class AddNoteEntityFragment: BaseFragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: AddNoteEntityFragmentArgs by navArgs()
    private val selectedNoteEntity: NoteEntity? by lazy {
        sharedViewModel.noteEntitiesLiveData.value?.find {
            it.id == safeArgs.selectedItemEntityId
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
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDate = System.currentTimeMillis()
        binding.performedDateTextView.text = dateFormatter(currentDate)

        showKeyboard()
        sharedViewModel.transactionCompletedLiveData.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
                if (isInEditMode) {
                    navigateUp()
                    return@observe
                }
                refreshAddItemEntityScreen()
            }
        }
        binding.titleEditText.requestFocus()
        // Setup screen if we are in edit mode
        setupSelectedItemEntity()
    }

    private fun saveNoteEntityToTheDatabase() {
        val noteTitle = binding.titleEditText.text.toString().trim()
        if(noteTitle.isEmpty()) {
            binding.titleEditText.error = "* Required field"
            return
        } else {
            binding.titleEditText.error = null
        }

        var noteDescription: String? = binding.descriptionEditText.text.toString().trim()
        if(noteDescription?.isEmpty() == true) {
            noteDescription = null
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = binding.performedDateTextView.text.toString()
        val notePerformDate: Long = dateFormat.parse(date).time

        if(isInEditMode) {
            val noteEntity = selectedNoteEntity!!.copy(
                title = noteTitle,
                description = noteDescription,
                performDate = notePerformDate
            )
            sharedViewModel.updateNote(noteEntity)
            return
        }

        val noteEntity = NoteEntity(
            id = UUID.randomUUID().toString(),
            title = noteTitle,
            description = noteDescription,
            performDate = notePerformDate,
            createdAt = System.currentTimeMillis()
        )
        sharedViewModel.insertNote(noteEntity)
    }

    private fun refreshAddItemEntityScreen() {
        binding.titleEditText.text = null
        binding.titleEditText.requestFocus()
        binding.descriptionEditText.text = null
        binding.performedDateTextView.text = dateFormatter(System.currentTimeMillis())
        Toast.makeText(requireActivity(), "Note successfully added!", Toast.LENGTH_SHORT).show()
    }

    private fun setupSelectedItemEntity() {
        selectedNoteEntity?.let { noteEntity ->
            isInEditMode = true

            binding.titleEditText.setText(noteEntity.title)
            binding.titleEditText.setSelection(noteEntity.title.length)
            binding.descriptionEditText.setText(noteEntity.description)
            binding.performedDateTextView.setText(dateFormatter(noteEntity.performDate))
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onMenuAddClicked() {
        saveNoteEntityToTheDatabase()
        if(isInEditMode) {
            Toast.makeText(requireActivity(), "Updated!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "Added!", Toast.LENGTH_SHORT).show()
        }
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
            Toast.makeText(requireActivity(), "Date Picker Cancelled", Toast.LENGTH_LONG).show()
        }
    }

    private fun dateFormatter(date: Long): String {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        return dateFormatter.format(Date(date))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}