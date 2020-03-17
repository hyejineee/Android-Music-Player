package com.hyejineee.musicapp.data.repository

import com.hyejineee.musicapp.model.SongInfo
import io.reactivex.Observable
import org.koin.core.KoinComponent

interface SongInfoRepository : KoinComponent {
    fun getSongInfo(): Observable<SongInfo>
}
