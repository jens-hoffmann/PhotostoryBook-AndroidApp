package org.jhoffmann.photostorybook.repositories

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jhoffmann.photostorybook.helpers.PSImage
import org.jhoffmann.photostorybook.helpers.PhotoStory
import java.io.File

object PhotoStoryRepository {

    private var applicationContext: Application? = null
    val localRepoStories = MutableLiveData<MutableList<PhotoStory>>()
    val localRepoFiles = MutableLiveData<MutableMap<PhotoStory, String>>()

    fun init(application: Application) {
        applicationContext = application
    }

    suspend fun loadPhotostories() {
        if (applicationContext != null) {
            val regex = """Photostory_\d+_.+\.json""".toRegex()
            withContext(Dispatchers.IO) {
                var files: Array<String> = applicationContext!!.fileList()
                val storyList = mutableListOf<PhotoStory>()
                val storyFiles = mutableMapOf<PhotoStory, String>()
                for (file in files) {
                    if (regex matches file) {
                        val fileContent = applicationContext!!.openFileInput(file).bufferedReader().use {
                            it.readText()
                        }
                        Log.i("xxx", fileContent)
                        val pStory: PhotoStory = Json.decodeFromString(fileContent)
                        storyList.add(pStory)
                        storyFiles[pStory] = file
                    }
                }
                localRepoStories.postValue(storyList)
                localRepoFiles.postValue(storyFiles)
            }
        }
    }

    suspend fun writePhotostory(photostory: PhotoStory) {
        if (applicationContext != null) {
            withContext(Dispatchers.IO) {
                var files: Array<String> = applicationContext!!.fileList()
                val s = Json.encodeToString(PhotoStory.serializer(), photostory)
                val tsLong = System.currentTimeMillis() / 1000
                val filename = "Photostory_" + tsLong.toString() + "_" + photostory.storyTitle + ".json"
                val file = File(applicationContext!!.filesDir, filename)
                applicationContext!!.openFileOutput(filename, Context.MODE_PRIVATE).use {
                    it.write(s.toByteArray())
                }
                localRepoFiles.value!![photostory] = filename
            }
        }
    }

    suspend fun deletePhotostory(photostory: PhotoStory) {
        if (applicationContext != null) {
            withContext(Dispatchers.IO) {
                val filename: String = localRepoFiles.value!![photostory] ?: return@withContext
                val file = File(applicationContext!!.filesDir, filename)
                file.delete()
                localRepoFiles.value!!.remove(photostory)
                localRepoStories.value!!.remove(photostory)
            }
        }
    }
    
}


