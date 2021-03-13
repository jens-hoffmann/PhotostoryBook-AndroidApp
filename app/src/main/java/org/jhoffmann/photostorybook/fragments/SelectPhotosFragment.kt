package org.jhoffmann.photostorybook.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.adapters.GalleryImageAdapter
import org.jhoffmann.photostorybook.adapters.PSImageClickListener
import org.jhoffmann.photostorybook.databinding.FragmentGalleryBinding
import org.jhoffmann.photostorybook.databinding.FragmentSelectPhotosBinding
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel

class SelectPhotosFragment : Fragment() {
    private lateinit var viewModel : PhotoStoryViewModel
    private lateinit var galleryAdapter: GalleryImageAdapter
    private val SPAN_COUNT = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSelectPhotosBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_select_photos, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoStoryViewModel::class.java)

        val recyclerView = binding.photosRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)        // init adapter

        galleryAdapter = GalleryImageAdapter(viewModel.localPhotos.value!!, viewModel.currentPhotoStory.value!!.photos, false, PSImageClickListener { image_view, psImage ->
            if (! viewModel.currentPhotoStory.value!!.photos.contains(psImage)) {
                viewModel.currentPhotoStory.value!!.photos.add(psImage)
                image_view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary))
            }
            else {
                viewModel.currentPhotoStory.value!!.photos.remove(psImage)
                image_view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary))
            }
        })
        recyclerView.adapter = galleryAdapter

        binding.buttonContinue.setOnClickListener { view ->
            if (viewModel.currentPhotoStory.value!!.photos.size > 0) {
                view.findNavController().navigate(SelectPhotosFragmentDirections.actionSelectPhotosFragmentToReorderPhotosFragment())
            } else {
                Toast.makeText(context, "Need at least one photo for the story !", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonBack.setOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }
}