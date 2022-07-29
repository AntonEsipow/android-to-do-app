package com.bigtoapp.todo.ui.epoxy.models

import com.bigtoapp.todo.R
import com.bigtoapp.todo.databinding.ModelEmptyStateBinding
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel

class EmptyStateEpoxyModel(val title: String, val subTitle: String):
    ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state) {

    override fun ModelEmptyStateBinding.bind() {
        titleTextView.text = title
        subtitleTextView.text = subTitle
    }
}