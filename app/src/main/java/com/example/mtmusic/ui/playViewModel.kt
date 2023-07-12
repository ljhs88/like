package com.example.mtmusic.ui

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mtmusic.bean.musicInfo

class playViewModel(application: Application) : AndroidViewModel(application) {

    val musicData: MutableLiveData<MutableList<musicInfo>> by lazy {
        MutableLiveData<MutableList<musicInfo>>()
    }

    fun searchStore() {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%/storage/%")

        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor = getApplication<Application>().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        val dataList = mutableListOf<musicInfo>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))// 专辑
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

                // 处理获取到的音乐文件信息
                val musicInfo = musicInfo(id, title, artist, album, duration, false)
                dataList.add(musicInfo)

            } while (cursor.moveToNext())
        }

        cursor?.close()
        musicData.value = dataList
    }

}