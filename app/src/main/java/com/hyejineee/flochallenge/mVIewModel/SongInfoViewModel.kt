package com.hyejineee.flochallenge.mVIewModel

import androidx.lifecycle.ViewModel
import com.hyejineee.flochallenge.data.repository.SongInfoRepository
import com.hyejineee.flochallenge.model.Lyrics
import com.hyejineee.flochallenge.model.SongInfo
import com.hyejineee.flochallenge.model.convertSrtToLyricsList
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class SongInfoViewModel(
    private val songInfoRepository: SongInfoRepository
) : ViewModel() {

    var songInfo: SongInfo = SongInfo()
        set(value) {
            field = value
            lyricsList = value.convertSrtToLyricsList()
            songInfoSubject.onNext(songInfo)
        }
    val songInfoSubject: Subject<SongInfo> = PublishSubject.create()
    val currentLyricsIndexSubject: Subject<Int> = PublishSubject.create()
    var lyricsList: List<Lyrics> = listOf()

    fun getSongInfo() = songInfoRepository.getSongInfo()

    fun setPosition(position: Long) {
        val currentIndex = lyricsList.find {
            (it.startTime / 100).toInt() <= (position / 100).toInt()
                    && (it.endTime / 100).toInt() > (position / 100).toInt()
        }?.index ?: 0
        currentLyricsIndexSubject.onNext(currentIndex)
    }

}
