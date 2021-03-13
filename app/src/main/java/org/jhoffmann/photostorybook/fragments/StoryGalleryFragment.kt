package org.jhoffmann.photostorybook.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.adapters.GalleryImageAdapter
import org.jhoffmann.photostorybook.adapters.PSImageClickListener
import org.jhoffmann.photostorybook.databinding.FragmentStoryGalleryBinding
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel

class StoryGalleryFragment : Fragment() {
    private lateinit var viewModel : PhotoStoryViewModel
    private lateinit var galleryAdapter: GalleryImageAdapter
    private val SPAN_COUNT = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentStoryGalleryBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_story_gallery, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoStoryViewModel::class.java)

        val recyclerView = binding.photosRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)        // init adapter

        galleryAdapter = GalleryImageAdapter(viewModel.currentPhotoStory.value!!.photos, null, false, PSImageClickListener { image_view, psImage ->
            Log.i("xxx", "Image CLICK 2 $image_view")
            requireView().findNavController().navigate(StoryGalleryFragmentDirections.actionStoryGalleryFragmentToViewPagerFragment())
        })
        recyclerView.adapter = galleryAdapter

        binding.buttonBack.setOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        binding.buttonFinish.setOnClickListener { view ->
            viewModel.addCurrentPhotostorytoList()
            view.findNavController().navigate(StoryGalleryFragmentDirections.actionStoryGalleryFragmentToStoryList())
        }

        return binding.root
    }
}