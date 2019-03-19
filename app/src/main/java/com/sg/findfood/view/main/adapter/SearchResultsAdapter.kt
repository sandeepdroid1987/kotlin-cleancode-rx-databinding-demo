package com.sg.findfood.view.main.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import com.sg.findfood.model.remote.Venue
import com.sg.findfood.viewmodel.MainViewModel

/**
 * created by sandeep gupta on 17/3/19
 */

class SearchResultsAdapter(@LayoutRes layout_id: Int, view_model: MainViewModel) :
    RecyclerView.Adapter<VenueViewHolder>() {

    var layoutId: Int? = null
    var venues: List<Venue> = ArrayList();
    var viewModel: MainViewModel? = null;

    init {
        layoutId = layout_id;
        viewModel = view_model;
    }

    override fun getItemCount(): Int {
        return venues.let { it!!.size }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder =
        VenueViewHolder.newInstance(parent)


    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bind(viewModel, position);
    }

    override fun getItemViewType(position: Int): Int {
        return layoutId!!
    }

    fun setData(venues: List<Venue>) {
        this.venues = venues!!
        notifyDataSetChanged()
    }

    fun getVenueAt(pos: Int): Venue? {
        return venues?.get(pos)
    }

}