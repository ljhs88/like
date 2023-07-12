package com.example.mtmusic.bean

data class musicInfo(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    var like: Boolean)
