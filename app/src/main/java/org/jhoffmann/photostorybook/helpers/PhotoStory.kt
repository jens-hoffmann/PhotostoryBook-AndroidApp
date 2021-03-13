package org.jhoffmann.photostorybook.helpers

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.serialization.*
import java.util.*

@Serializable
data class PSImage(var imageUrl: String,
                   var title: String) {
}

@Serializable
data class PhotoStory(val photoStoryUUID: String,
                      var storyTitle: String,
                      var titleImage: PSImage?,
                      var photos: MutableList<PSImage>) {




}
