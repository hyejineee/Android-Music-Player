package com.hyejineee.flochallenge.data.dataSource

import com.hyejineee.flochallenge.data.dataSource.serviceAPI.FloServiceAPI
import com.hyejineee.flochallenge.model.SongInfo
import com.hyejineee.flochallenge.model.convertLyricsStringToSRTString
import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject

class RemoteSongInfoDataSource : KoinComponent {
    private val floServiceAPI: FloServiceAPI by inject()

    fun get(): Observable<SongInfo> {
        return Observable.create { observer ->
            try {
                val response = floServiceAPI
                    .getSongInfoService()
                    .getSongInfo()
                    .execute()

                response.body()?.apply {
                    this.lyrics = this.convertLyricsStringToSRTString()
                }

                when (response.code()) {
                    200 -> observer.onNext(response.body() ?: SongInfo())
                    else -> observer.onError(Error(response.message()))
                }
                observer.onComplete()
            } catch (e: Throwable) {
                observer.onError(Error(e))
                observer.onComplete()
            }
        }
    }

}
