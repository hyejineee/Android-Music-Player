package com.hyejineee.musicApp.data.repository

import com.hyejineee.musicApp.model.SongInfo
import io.reactivex.Observable
import org.koin.core.KoinComponent

interface SongInfoRepository : KoinComponent {
    fun getSongInfo(): Observable<SongInfo>
}
