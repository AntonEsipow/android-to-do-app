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
        val categoriesFragmentHeader = getString(R.string.categories_fragment_header)
    }
}