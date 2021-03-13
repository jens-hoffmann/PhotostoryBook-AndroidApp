package org.jhoffmann.photostorybook.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.databinding.ItemGalleryImageBinding
import org.jhoffmann.photostorybook.helpers.GlideApp
import org.jhoffmann.photostorybook.helpers.PSImage
import org.jhoffmann.photostorybook.helpers.PhotoStory
import java.security.KeyStore


class GalleryImageAdapter(private val itemList: MutableList<PSImage>, val selectedItems: MutableList<PSImage>?, val showImageNumber: Boolean, val clickListener: PSImageClickListener) :
        RecyclerView.Adapter<GalleryImageAdapter.MyViewHolder>() {

    val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter as GalleryImageAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                adapter.moveItem(from, to)
                adapter.notifyItemMoved(from, to)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)

                viewHolder?.itemView?.alpha = 1.0f
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemGalleryImageBinding>(inflater, R.layout.item_gallery_image, parent, false)
        
        binding.galleryItemContainer.setOnTouchListener { v, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                startDragging(MyViewHolder(binding))
            }
            return@setOnTouchListener true
        }

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: GalleryImageAdapter.MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, clickListener)
    }

    fun moveItem(from: Int, to: Int) {
        val fromPhoto = itemList[from]
        itemList.removeAt(from)
        if (to < from) {
            itemList.add(to, fromPhoto)
        } else {
            itemList.add(to - 1, fromPhoto)
        }
    }

        inner class MyViewHolder(val binding: ItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root)  {
        val imgGallery: ImageView  = binding.ivGalleryImage
        val txtNumber: TextView = binding.txtImageNumber

        fun bind(item: PSImage, clickListener: PSImageClickListener) {
            binding.psImage = item
            if (showImageNumber) {
                txtNumber.text = adapterPosition.toString()
                txtNumber.visibility = View.VISIBLE
            }
            if (selectedItems != null)
                if (selectedItems.contains(item)) {
                        imgGallery.setBackgroundColor(Color.GREEN)
                    } else {
                        imgGallery.setBackgroundColor(Color.WHITE)
                    }


            GlideApp.with(binding.root)
                    .load(item.imageUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgGallery)

            binding.imageClickListener  = clickListener

        }
    }
}

class PSImageClickListener(val clickListener: (v: View, psImage: PSImage) -> Unit) {
    fun onClick(v: View, psImage: PSImage) = clickListener(v, psImage)
}