package com.bigtoapp.todo.ui.category

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyController
import com.bigtoapp.todo.Localizations
import com.bigtoapp.todo.R
import com.bigtoapp.todo.addHeaderModel
import com.bigtoapp.todo.database.entity.CategoryEntity
import com.bigtoapp.todo.databinding.ModelCategoryEntityBinding
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel
import com.bigtoapp.todo.ui.epoxy.models.EmptyStateEpoxyModel
import com.bigtoapp.todo.ui.epoxy.models.LoadingEpoxyModel
import kotlin.math.roundToInt

class CategoryEpoxyController(
    private val categoryEntityInterface: CategoryEntityInterface
): EpoxyController() {

    private var isLoading: Boolean = true
        set(value) {
            field = value
            if(field) {
                requestModelBuild()
            }
        }

    var categoryEntityList = ArrayList<CategoryEntity>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {

        if(isLoading) {
            LoadingEpoxyModel().id("loading_state").addTo(this)
            return
        }

        if(categoryEntityList.isEmpty()) {
            val title = Localizations.noCategoryTitle
            val subtitle = Localizations.noCategorySub
            EmptyStateEpoxyModel(title, subtitle).id("empty_state").addTo(this)
            return
        }

        addHeaderModel(Localizations.allCategoriesHeader)
        categoryEntityList.forEach { category ->
            CategoryEpoxyController
                .CategoryEntityEpoxyModel(category, categoryEntityInterface).id(category.id).addTo(this)
        }
    }

    data class CategoryEntityEpoxyModel(
        val categoryEntity: CategoryEntity,
        val categoryEntityInterface: CategoryEntityInterface
    ): ViewBindingKotlinModel<ModelCategoryEntityBinding>(R.layout.model_category_entity) {

        override fun ModelCategoryEntityBinding.bind() {

            nameCategoryText.text = categoryEntity.name

            categoryView.setBackgroundColor(categoryEntity.color)
            root.setStrokeColor(ColorStateList.valueOf(categoryEntity.color))

            root.setOnClickListener {
                categoryEntityInterface.onCategorySelected(categoryEntity)
            }
        }
    }
}