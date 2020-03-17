package com.hyejineee.flochallenge.data.repository

import com.hyejineee.flochallenge.model.SongInfo
import io.reactivex.Observable
import org.koin.core.KoinComponent

interface SongInfoRepository : KoinComponent {
    fun getSongInfo(): Observable<SongInfo>
}
