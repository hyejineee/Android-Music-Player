package com.hyejineee.musicApp.data.dataSource.serviceAPI

import com.hyejineee.musicApp.model.SongInfo
import retrofit2.Call
import retrofit2.http.GET

interface SongInfoAPI {
    @GET("/2020-flo/song.json")
    fun getSongInfo(): Call<SongInfo>
}
