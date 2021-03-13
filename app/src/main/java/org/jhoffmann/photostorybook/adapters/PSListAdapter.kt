package org.jhoffmann.photostorybook.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.helpers.PhotoStory
import org.jhoffmann.photostorybook.databinding.StorylistItemBinding
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel


class PSListAdapter(val clickListener: PSClickListener) : RecyclerView.Adapter<PSListAdapter.PSViewHolder>() {

    var data = listOf<PhotoStory>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PSViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StorylistItemBinding>(inflater, R.layout.storylist_item, parent, false)

        return PSViewHolder(binding)

    }

    override fun onBindViewHolder(holder: PSViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, clickListener)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    inner class PSViewHolder(val binding: StorylistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //val imgStoryTitle: ImageView  = binding.imgStoryTitle


        fun bind(item: PhotoStory, clickListener: PSClickListener) {
            Log.i("XXX", "BINDING $item")
            binding.photoStory = item
            binding.clickListener = clickListener

        }
    }
}

class PSClickListener(val clickListener: (photoStory: PhotoStory, target: View) -> Unit) {
    fun onClick(photoStory: PhotoStory, target: View) = clickListener(photoStory, target)
}