package com.hyejineee.musicApp.data.repository

import com.hyejineee.musicApp.data.dataSource.RemoteSongInfoDataSource
import org.koin.core.inject

class SongInfoRepositoryImpl : SongInfoRepository {

    private val remoteSongInfoDataSource: RemoteSongInfoDataSource by inject()

    override fun getSongInfo() = remoteSongInfoDataSource.get()
}
