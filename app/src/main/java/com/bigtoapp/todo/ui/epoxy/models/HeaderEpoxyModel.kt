package com.bigtoapp.todo.ui.epoxy.models

import com.bigtoapp.todo.R
import com.bigtoapp.todo.databinding.ModelHeaderItemBinding
import com.bigtoapp.todo.ui.epoxy.ViewBindingKotlinModel

class HeaderEpoxyModel(
    private val headerText: String
): ViewBindingKotlinModel<ModelHeaderItemBinding>(R.layout.model_header_item) {

    override fun ModelHeaderItemBinding.bind() {
        textView.text = headerText
    }

//    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
//        return totalSpanCount
//    }
}