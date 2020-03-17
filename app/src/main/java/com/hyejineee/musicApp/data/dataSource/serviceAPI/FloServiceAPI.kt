package com.hyejineee.musicApp.data.dataSource.serviceAPI

import retrofit2.Retrofit

class FloServiceAPI(val retrofit: Retrofit) {

    fun getSongInfoService(): SongInfoAPI = retrofit.create(SongInfoAPI::class.java)

}