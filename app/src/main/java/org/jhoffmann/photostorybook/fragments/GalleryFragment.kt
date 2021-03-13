package org.jhoffmann.photostorybook.fragments

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.adapters.*
import org.jhoffmann.photostorybook.helpers.PSImage
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel
import org.jhoffmann.photostorybook.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {
    // gallery column count
    private val SPAN_COUNT = 3

    private lateinit var viewModel : PhotoStoryViewModel
    private lateinit var galleryAdapter: GalleryImageAdapter
    lateinit var clickedPhoto: PSImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGalleryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(requireActivity()).get(PhotoStoryViewModel::class.java)

        val recyclerView = binding.galleryRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)        // init adapter

        galleryAdapter = GalleryImageAdapter(viewModel.localPhotos.value!!, null, false, PSImageClickListener { image_view, psImage ->
            Log.i("xxx", "Image CLICK $image_view")
            viewModel.setTitleImage(psImage)
            clickedPhoto = psImage
            binding.root.findNavController().navigate(GalleryFragmentDirections.actionGalleryFragment2ToPSTitleEditFragment2())


        })

        recyclerView.adapter = galleryAdapter

        return binding.root
    }
}