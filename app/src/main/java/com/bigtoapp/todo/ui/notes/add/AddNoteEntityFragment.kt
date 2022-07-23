package com.bigtoapp.todo.ui.notes.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bigtoapp.todo.R
import com.bigtoapp.todo.database.entity.NoteEntity
import com.bigtoapp.todo.databinding.FragmentAddNoteBinding
import com.bigtoapp.todo.ui.BaseFragment
import java.util.*

private const val MY_HINT= "Title"

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

        if(isInEditMode) {
            val noteEntity = selectedNoteEntity!!.copy(
                title = noteTitle,
                description = noteDescription,
            )
            sharedViewModel.updateNote(noteEntity)
            return
        }

        val noteEntity = NoteEntity(
            id = UUID.randomUUID().toString(),
            title = noteTitle,
            description = noteDescription,
            createdAt = System.currentTimeMillis()
        )
        sharedViewModel.insertNote(noteEntity)
    }

    private fun refreshAddItemEntityScreen() {
        binding.titleEditText.text = null
        binding.titleEditText.requestFocus()
        binding.descriptionEditText.text = null
        Toast.makeText(requireActivity(), "Note successfully added!", Toast.LENGTH_SHORT).show()
    }

    private fun setupSelectedItemEntity() {
        selectedNoteEntity?.let { noteEntity ->
            isInEditMode = true

            binding.titleEditText.setText(noteEntity.title)
            binding.titleEditText.setSelection(noteEntity.title.length)
            binding.descriptionEditText.setText(noteEntity.description)
            mainActivity.supportActionBar?.title = "Update item"
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == R.id.menuItemAdd) {
            saveNoteEntityToTheDatabase()
            if(isInEditMode) {
                Toast.makeText(requireActivity(), "Updated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Added!", Toast.LENGTH_SHORT).show()
            }
            true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}