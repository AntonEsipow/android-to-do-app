package com.bigtoapp.todo.ui.epoxy.models

import com.bigtoapp.todo.R
import com.bigtoapp.todo.databinding.ModelEmptyStateBinding
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel

class EmptyStateEpoxyModel:
    ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state) {

    override fun ModelEmptyStateBinding.bind() {
        // todo nothing in the moment
    }
}