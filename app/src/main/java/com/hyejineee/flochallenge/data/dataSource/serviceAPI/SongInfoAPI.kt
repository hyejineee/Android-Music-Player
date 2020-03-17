package com.hyejineee.flochallenge.data.dataSource.serviceAPI

import com.hyejineee.flochallenge.model.SongInfo
import retrofit2.Call
import retrofit2.http.GET

interface SongInfoAPI {
    @GET("/2020-flo/song.json")
    fun getSongInfo(): Call<SongInfo>
}
