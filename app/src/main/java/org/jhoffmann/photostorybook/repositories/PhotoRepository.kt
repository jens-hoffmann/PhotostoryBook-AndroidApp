package org.jhoffmann.photostorybook.repositories

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jhoffmann.photostorybook.helpers.PSImage

object PhotoRepository {

    private var applicationContext: Application? = null
    val localRepoPhotos = MutableLiveData<MutableList<PSImage>>()

    fun init(application: Application) {
        applicationContext = application
    }

    suspend fun loadPhotos() {
        if (applicationContext != null) {
            withContext(Dispatchers.IO) {
                val imageList = mutableListOf<PSImage>()

                val collection =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            MediaStore.Images.Media.getContentUri(
                                    MediaStore.VOLUME_EXTERNAL
                            )
                        } else {
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                val projection = arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED
                )

                val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

                val query = applicationContext!!.contentResolver.query(collection, projection, null, null, sortOrder)

                query?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

                    while (cursor.moveToNext()) {
                        val imageId = cursor.getLong(idColumn)
                        val imageTitle = cursor.getString(titleColumn)
                        val contentUri: Uri = ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                imageId
                        )

                        imageList.add(PSImage(contentUri.toString(), imageTitle))
                    }
                }
                localRepoPhotos.postValue(imageList)
            }

        }
    }
}