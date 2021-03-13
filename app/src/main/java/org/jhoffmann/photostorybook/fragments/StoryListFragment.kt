package org.jhoffmann.photostorybook.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.jhoffmann.photostorybook.R
import org.jhoffmann.photostorybook.adapters.PSClickListener
import org.jhoffmann.photostorybook.adapters.PSListAdapter
import org.jhoffmann.photostorybook.databinding.FragmentStoryListBinding
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel

class StoryListFragment : Fragment() {


    private lateinit var viewModel : PhotoStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentStoryListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_story_list, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(PhotoStoryViewModel::class.java)

        //val newfrag = inflater.inflate(, container, false)
        val divider = DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        val recycStoryList = binding.recycleviewStorylist
        Log.i("XXX", "recycStoryList: $recycStoryList")
        recycStoryList.layoutManager = LinearLayoutManager(context)
        recycStoryList.addItemDecoration(divider)
        binding.lifecycleOwner = this
        binding.photoStoryViewModel = viewModel

        setHasOptionsMenu(true)

        val adapter = PSListAdapter(PSClickListener { photoStory, target ->
            viewModel.setCurrentPhotoStory(photoStory)
            val navContr = this.findNavController()
            if (target.tag == "startPS")
                navContr.navigate(StoryListFragmentDirections.actionStoryListToPSIntroFragment())
            else if (target.tag == "deletePS") {
                AlertDialog.Builder(context)
                        .setTitle(R.string.delete_story)
                        .setMessage(R.string.delete_story_question) // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                            Log.i("xxx", "Delete $which : $photoStory")
                            viewModel.deletePhotostory(photoStory)
                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()

            }
            else if (target.tag == "editPS")
                navContr.navigate(StoryListFragmentDirections.actionStoryListToPSTitleEditFragment2())
        })
        binding.recycleviewStorylist.adapter = adapter


        viewModel.photoStories.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }

        })

        val fabAddStory = binding.floatingActionButtonAddStory
        fabAddStory.setOnClickListener { view ->
            viewModel.createEmptyPhotoStory()
            val navContr = view.findNavController()
            navContr.navigate(R.id.action_storyList_to_PSTitleEditFragment2)
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }


}