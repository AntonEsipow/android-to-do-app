package com.bigtoapp.todo.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bigtoapp.todo.arch.ToDoViewModel
import com.bigtoapp.todo.database.AppDatabase

abstract class BaseFragment: Fragment() {

    protected val mainActivity: MainActivity
        get() = activity as MainActivity

    protected val appDataBase: AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())

    protected val sharedViewModel: ToDoViewModel by activityViewModels()
}