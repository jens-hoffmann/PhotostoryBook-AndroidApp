package org.jhoffmann.photostorybook.helpers

import android.util.Log
import android.widget.ImageView
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jhoffmann.photostorybook.R
import java.util.UUID
import org.jhoffmann.photostorybook.BR
import org.jhoffmann.photostorybook.repositories.PhotoRepository
import org.jhoffmann.photostorybook.repositories.PhotoStoryRepository

class PhotoStoryViewModel : ObservableViewModel() {

    val viewModelJob = SupervisorJob()
    val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _currentPhotoStory = MutableLiveData<PhotoStory>()
    val currentPhotoStory: LiveData<PhotoStory>
        get() = _currentPhotoStory

    private val _photoStories = MutableLiveData<MutableList<PhotoStory>>()
    val photoStories: LiveData<MutableList<PhotoStory>>
        get() = PhotoStoryRepository.localRepoStories

    private val _localPhotos = MutableLiveData<MutableList<PSImage>>()
    val localPhotos: LiveData<MutableList<PSImage>>
        get() = PhotoRepository.localRepoPhotos

    fun createEmptyPhotoStory() {
        val ps =  PhotoStory(UUID.randomUUID().toString(), "", null , mutableListOf<PSImage>())
        _currentPhotoStory.value = ps
    }

    fun addPhotoStoryToList(photoStory: PhotoStory) {
        if (photoStory.storyTitle.length > 0) {
            _photoStories.value!!.add(photoStory)
            this.notifyChange()
        }
    }

    fun addCurrentPhotostorytoList() {
        if (currentPhotoStory.value!!.storyTitle.length > 0) {
            viewModelScope.launch(Dispatchers.IO) {
                PhotoStoryRepository.writePhotostory(currentPhotoStory.value!!)
            }
            _photoStories.value!!.add(currentPhotoStory.value!!)
            this.notifyChange()
        }
    }

    fun deletePhotostory(photoStory: PhotoStory) {
        viewModelScope.launch(Dispatchers.IO) {
            PhotoStoryRepository.deletePhotostory(photoStory)
        }
        this.notifyChange()
    }

    var photoStoryTitle: String
        @Bindable get() {
            return _currentPhotoStory.value!!.storyTitle
        }
        set(value) {
            // Avoids infinite loops.
            if (_currentPhotoStory.value!!.storyTitle != value) {
                _currentPhotoStory.value!!.storyTitle = value

                // Notify observers of a new value.
                notifyPropertyChanged(BR.photoStoryTitle)
            }
        }


    fun setTitleImage(image: PSImage) {
        _currentPhotoStory.value!!.titleImage = image
    }





    fun setCurrentPhotoStory(photoStory: PhotoStory) {
        _currentPhotoStory.value = photoStory
    }


    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("PhotoStoryViewModel", "PhotoStoryViewModel destroyed")
    }


    init {
        Log.i("PhotoStoryViewModel", "PhotoStoryViewModel created.")
        _photoStories.value = mutableListOf<PhotoStory>()
    }



}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    if (url != null) {
        Glide.with(imageView.context).load(url).into(imageView)
    } else {
        Glide.with(imageView.context).load("").placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(imageView)
    }

}