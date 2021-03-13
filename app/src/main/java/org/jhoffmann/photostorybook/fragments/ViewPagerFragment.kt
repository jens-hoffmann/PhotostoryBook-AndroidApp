package org.jhoffmann.photostorybook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.adapters.GalleryImageAdapter
import org.jhoffmann.photostorybook.adapters.ImagePagerAdapter
import org.jhoffmann.photostorybook.databinding.FragmentGalleryBinding
import org.jhoffmann.photostorybook.databinding.FragmentViewPagerBinding
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel

class ViewPagerFragment : Fragment() {
    private lateinit var viewModel : PhotoStoryViewModel
    private lateinit var pagerAdapter: ImagePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentViewPagerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(requireActivity()).get(PhotoStoryViewModel::class.java)

        pagerAdapter = ImagePagerAdapter(viewModel.currentPhotoStory.value!!.photos, true)
        binding.photosPager.adapter = pagerAdapter

        return binding.root
    }

}