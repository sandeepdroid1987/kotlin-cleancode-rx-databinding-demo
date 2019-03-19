package com.sg.findfood.view.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sg.findfood.R
import com.sg.findfood.model.remote.Icon
import com.sg.findfood.view.main.adapter.VenueViewHolder

/**
 * created by sandeep gupta on 17/3/19
 */

class CustomViewBindings {

    companion object {
        @BindingAdapter("setAdapter")
        @JvmStatic
        fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<VenueViewHolder>) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.getContext())
            recyclerView.adapter = adapter
        }

        @BindingAdapter("imageurl")
        @JvmStatic
        fun bindUrl(imageView: ImageView, icon: Icon?) {

            var imageUrl: String? = null
            if (".png" == icon?.suffix) {
                imageUrl = icon.prefix + "88" + icon.suffix;
            }

            if (imageUrl != null) {
                // If we don't do this, you'll see the old image appear briefly
                // before it's replaced with the current image
                if (imageView.getTag(R.id.icon) == null || !imageView.getTag(R.id.icon).equals(imageUrl)) {
                    imageView.setImageBitmap(null);
                    imageView.setTag(R.id.icon, imageUrl);
                    Glide.with(imageView.context).load(imageUrl).into(imageView);
                }
            } else {
                imageView.setTag(R.id.icon, null);
                imageView.setImageBitmap(null)
            }
        }

        @BindingAdapter("setTextAndVisibility")
        @JvmStatic
        fun bindTextViewVisiblity(textView: TextView, text: String?) {

            if (text != null) {
                textView.also { it.visibility = View.VISIBLE }.also { it.text = text }
            } else {
                textView.also { it.visibility = View.GONE }.also { it.text = null }
            }
        }

        @BindingAdapter("setFavState")
        @JvmStatic
        fun bindFavState(imageView: ImageView, isFav: Boolean) {

            if (isFav!!) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(imageView.context, R.drawable.ic_baseline_favorite_24px)
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(imageView.context, R.drawable.ic_baseline_favorite_border_24px)
                )
            }
        }

        @BindingAdapter("setMessage")
        @JvmStatic
        fun bindMessage(textView: TextView, count : Int) {
            when (count) {
                Constants.STATE_BLANK -> textView.text =null
                Constants.STATE_ERROR -> textView.apply{this.text = "Oops something went wrong!\nPlease check your connection and try again"}
                    .apply { this.setTextColor(ContextCompat.getColor(this.context,R.color.error_color_material_dark)) }
                0 ->textView.apply{this.text = "No matching results found! Please try again."}
                    .apply { this.setTextColor(ContextCompat.getColor(this.context,R.color.abc_primary_text_material_light)) }

                else -> { // Note the block
                    textView.apply{this.text = String.format("%d results found." , count)}
                        .apply { this.setTextColor(ContextCompat.getColor(this.context,R.color.abc_primary_text_material_light)) }

                }
            }

        }
    }
}