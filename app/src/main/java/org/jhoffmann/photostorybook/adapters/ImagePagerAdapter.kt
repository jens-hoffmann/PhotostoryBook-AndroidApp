package org.jhoffmann.photostorybook.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.helpers.PSImage
import org.jhoffmann.photostorybook.databinding.ItemViewpagerBinding

class ImagePagerAdapter(private var listPhotos: MutableList<PSImage>?, val editTitle: Boolean) :
RecyclerView.Adapter<ImagePagerAdapter.MyViewHolder>() {

    private lateinit var context: Context

   inner class MyViewHolder(val itemBinding: ItemViewpagerBinding) : RecyclerView.ViewHolder(itemBinding.root){
       private var binding : ItemViewpagerBinding? = null

       init {
           this.binding = itemBinding
       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemViewpagerBinding>(inflater, R.layout.item_viewpager, parent, false)
        if (editTitle == true) {
            binding.editPhotoTitle.visibility = View.VISIBLE
            binding.txtPhotoTitle.visibility = View.GONE
        } else {
            binding.editPhotoTitle.visibility = View.GONE
            binding.txtPhotoTitle.visibility = View.VISIBLE
        }


        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        listPhotos?.let {
            return it.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemBinding.psImage = listPhotos?.get(position)
    }
}