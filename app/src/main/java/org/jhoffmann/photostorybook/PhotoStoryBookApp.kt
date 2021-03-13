package org.jhoffmann.photostorybook

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jhoffmann.photostorybook.databinding.ActivityMainBinding
import org.jhoffmann.photostorybook.helpers.PSImage
import org.jhoffmann.photostorybook.helpers.PhotoStory
import org.jhoffmann.photostorybook.helpers.PhotoStoryViewModel
import org.jhoffmann.photostorybook.repositories.PhotoRepository
import org.jhoffmann.photostorybook.repositories.PhotoStoryRepository
import java.util.UUID

class PhotoStoryBookApp : AppCompatActivity() {

    private lateinit var viewModel : PhotoStoryViewModel
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        PhotoRepository.init(application)
        PhotoStoryRepository.init(application)

        val viewModelJob = SupervisorJob()
        val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
        viewModelScope.launch {
            PhotoRepository.loadPhotos()
            PhotoStoryRepository.loadPhotostories()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val check = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (check == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123)
        }

        // Navigation Controller einrichten

        //findViewById<View>(R.id.fragHost).post {
        binding.fragHost.post {
            val navController = this.findNavController(R.id.fragHost)
            NavigationUI.setupActionBarWithNavController(this, navController)
        }


        // View Model initialisieren (aber nur einmal!)
        viewModel = ViewModelProvider(this).get(PhotoStoryViewModel::class.java)

        viewModel.addPhotoStoryToList(PhotoStory(UUID.randomUUID().toString(), "my first story", null, mutableListOf(PSImage("image1.jpg", "first"),
                PSImage("image2.jpg", "second"),
                PSImage("image3.jpg", "third"))))
        viewModel.addPhotoStoryToList(PhotoStory(UUID.randomUUID().toString(),"my seconds story", null, mutableListOf(PSImage("image1.jpg", "first"),
                PSImage("image2.jpg", "second"),
                PSImage("image3.jpg", "third"))))
        //val s = Json.encodeToString(ps.toList())




    }

    override fun onSupportNavigateUp(): Boolean {
        Log.i("xxx", "onSupportNavigateUp in MainActivity")
        val navController = this.findNavController(R.id.fragHost)
        return navController.navigateUp()
    }

}