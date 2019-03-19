package com.sg.findfood.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sg.findfood.R
import com.sg.findfood.databinding.ListItemBinding
import com.sg.findfood.viewmodel.MainViewModel

/**
 * created by sandeep gupta on 17/3/19
 */

class VenueViewHolder(var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: MainViewModel?, position: Int) {
        binding.position = position
        binding.viewModel = viewModel
        binding.executePendingBindings();
    }

    companion object {

        fun newInstance(parent: ViewGroup): VenueViewHolder {
            val binding: ListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item,
                parent,
                false
            )
            return VenueViewHolder(binding)
        }
    }
}
