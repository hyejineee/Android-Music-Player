package com.hyejineee.flochallenge

import com.hyejineee.flochallenge.data.repository.SongInfoRepository
import com.hyejineee.flochallenge.mVIewModel.SongInfoViewModel
import com.hyejineee.flochallenge.model.SongInfo
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class SongInfoViewModelTest {
    private var songInfoRepository: SongInfoRepository = mock(SongInfoRepository::class.java)

    val songInfo = SongInfo(
        "챔버오케스트라",
        "캐롤 모음",
        "We Wish You A Merry Christmas",
        198,
        "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/cover.jpg",
        "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/music.mp3",
        "[00:16:200]we wish you a merry christmas\n[00:18:300]we wish you a merry christmas"
    )

    @BeforeEach
    fun setUp() {
        given(songInfoRepository.getSongInfo())
            .willReturn(Observable.just(songInfo))
    }

    @Test
    fun `setPositionTest`() {
        val viewmodel = SongInfoViewModel(songInfoRepository)

        val testObserver: TestObserver<Int> = TestObserver()
        viewmodel.currentLyricsIndexSubject.subscribe(testObserver)

        viewmodel.setPosition(16200)

        testObserver.awaitCount(1)
        assertThat(testObserver.values().first()).isEqualTo(0)
    }
}