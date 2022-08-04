package com.bigtoapp.todo

class Localizations {

    companion object {
        private fun getString(res: Int): String {
            return App.resourses.getString(res)
        }
        // Add CategoryEntity Fragment
        val rgbRed = getString(R.string.rgb_red)
        val rgbGreen = getString(R.string.rgb_green)
        val rgbBlue = getString(R.string.rgb_blue)
        val titleError = getString(R.string.title_error)
        val addSuccess = getString(R.string.add_success)
        val titleUpdateCategory = getString(R.string.title_update_category)
        val titleAddCategory = getString(R.string.title_add_category)
        // Category EpoxyController
        val noCategoryTitle = getString(R.string.title_no_category)
        val noCategorySub = getString(R.string.subtitle_no_category)
        val allCategoriesHeader = getString(R.string.header_all_categories)
        // Category Fragment
        val categoriesFragmentHeader = getString(R.string.fragment_title_categories)

        // Add NoteEntityFragment
        val noteUpdated = getString(R.string.note_updated)
        val noteAdded = getString(R.string.note_added)
        val fragmentTitleUpdateNote = getString(R.string.fragment_title_update_note)
        val fragmentTitleAddNote = getString(R.string.fragment_title_add_note)
        val selectPerformDate = getString(R.string.select_perform_date)
        val isSelected = getString(R.string.is_selected)
        val selectCategory = getString(R.string.select_category)
        val select = getString(R.string.select)
        val categorySelected = getString(R.string.category_selected)
        val cancel = getString(R.string.cancel)

        // Notes Fragment
        val fragmentNotesTitle = getString(R.string.fragment_notes_title)
        val titleNoNotes = getString(R.string.title_no_notes)
        val subtitleNoNotes = getString(R.string.subtitle_no_notes)

        // Sorting
        val sortByCategory = getString(R.string.sort_by_category)
        val sortByPerformDate = getString(R.string.sort_by_perform_date)
        val sortByCurrentDate = getString(R.string.sort_by_current_date)
        val sortDisplayAllNotes = getString(R.string.sort_display_all_notes)

        // Def category
        val defCategoryName = getString(R.string.def_category_name)

        // Day of week
        val monday = getString(R.string.monday)
        val tuesday = getString(R.string.tuesday)
        val wednesday = getString(R.string.wednesday)
        val thursday = getString(R.string.thursday)
        val friday = getString(R.string.friday)
        val saturday = getString(R.string.saturday)
        val sunday = getString(R.string.sunday)
    }
}