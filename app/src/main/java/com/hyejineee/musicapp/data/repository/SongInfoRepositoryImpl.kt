package com.hyejineee.musicapp.data.repository

import com.hyejineee.musicapp.data.dataSource.RemoteSongInfoDataSource
import org.koin.core.inject

class SongInfoRepositoryImpl : SongInfoRepository {

    private val remoteSongInfoDataSource: RemoteSongInfoDataSource by inject()

    override fun getSongInfo() = remoteSongInfoDataSource.get()
}
