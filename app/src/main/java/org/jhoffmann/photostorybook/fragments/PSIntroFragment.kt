package org.jhoffmann.photostorybook.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.databinding.FragmentPsIntroBinding
import org.jhoffmann.photostorybook.databinding.FragmentPsTitleEditBinding
import org.jhoffmann.photostorybook.helpers.PhotoStory
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel

class PSIntroFragment : Fragment() {
    private lateinit var viewModel : PhotoStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoStoryViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPsIntroBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_ps_intro, container, false )
        binding.photoStory = viewModel.currentPhotoStory.value

        binding.buttonContinue.setOnClickListener { view ->
            view.findNavController().navigate(PSIntroFragmentDirections.actionPSIntroFragmentToStorySlideShow())
        }
        binding.buttonBack.setOnClickListener { view ->
            view.findNavController().navigateUp()
        }
        return binding.root
    }
}