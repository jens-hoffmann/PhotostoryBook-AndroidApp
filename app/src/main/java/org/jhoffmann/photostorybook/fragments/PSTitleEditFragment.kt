package org.jhoffmann.photostorybook.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel
import org.jhoffmann.photostorybook.databinding.FragmentPsTitleEditBinding
import org.jhoffmann.photostorybook.databinding.FragmentStoryListBinding

class PSTitleEditFragment : Fragment() {
    private lateinit var viewModel : PhotoStoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoStoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPsTitleEditBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_ps_title_edit, container, false )
        binding.viewmodel = viewModel
        binding.photoStory = viewModel.currentPhotoStory.value


        binding.imageStoryTitle.setOnClickListener { view ->
            view.findNavController().navigate(PSTitleEditFragmentDirections.actionPSTitleEditFragment2ToGalleryFragment2())
        }
        binding.buttonContinue.setOnClickListener { view ->
            if (TextUtils.isEmpty(viewModel.currentPhotoStory.value!!.storyTitle)) {
                Toast.makeText(context, "Title of photostory is mandatory !", Toast.LENGTH_SHORT).show()

            } else {
                view.findNavController().navigate(PSTitleEditFragmentDirections.actionPSTitleEditFragment2ToSelectPhotosFragment())
            }
        }
        binding.buttonBack.setOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }
}