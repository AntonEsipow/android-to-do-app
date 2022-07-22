package com.bigtoapp.todo.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.bigtoapp.todo.arch.ToDoViewModel
import com.bigtoapp.todo.database.AppDatabase

abstract class BaseFragment: Fragment() {

    protected val mainActivity: MainActivity
        get() = activity as MainActivity

    protected val appDataBase: AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())

    protected val sharedViewModel: ToDoViewModel by activityViewModels()

    // region Navigation helper methods
    protected fun navigateUp() {
        mainActivity.navController.navigateUp()
    }

    protected fun navigateViaNavGraph(actionId: Int) {
        mainActivity.navController.navigate(actionId)
    }

    protected fun navigateViaNavGraph(navDirections: NavDirections) {
        mainActivity.navController.navigate(navDirections)
    }
    // endregion Navigation helper methods

    // region Keyboard methods
    fun Fragment.showKeyboard() {
        view?.let { activity?.showKeyboard(it) }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun Context.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // endregion Keyboard methods
}