package org.jhoffmann.photostorybook.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.adapters.GalleryImageAdapter
import org.jhoffmann.photostorybook.adapters.PSImageClickListener
import org.jhoffmann.photostorybook.databinding.FragmentReorderPhotosBinding
import org.jhoffmann.photostorybook.databinding.FragmentSelectPhotosBinding
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel

class ReorderPhotosFragment : Fragment() {
    private lateinit var viewModel : PhotoStoryViewModel
    private lateinit var galleryAdapter: GalleryImageAdapter
    private val SPAN_COUNT = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentReorderPhotosBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_reorder_photos, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoStoryViewModel::class.java)

        val recyclerView = binding.photosRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)        // init adapter

        galleryAdapter = GalleryImageAdapter(viewModel.currentPhotoStory.value!!.photos, null, true, PSImageClickListener { image_view, psImage ->
            Log.i("xxx", "Image CLICK 3 $psImage")
        })
        recyclerView.adapter = galleryAdapter
        binding.buttonBack.setOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        galleryAdapter.itemTouchHelper.attachToRecyclerView(recyclerView)

        binding.buttonContinue.setOnClickListener { view ->
            view.findNavController().navigate(ReorderPhotosFragmentDirections.actionReorderPhotosFragmentToStoryGalleryFragment())
        }
        binding.buttonBack.setOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }
}