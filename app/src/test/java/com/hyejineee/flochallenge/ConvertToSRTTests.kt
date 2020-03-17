package com.hyejineee.flochallenge

import com.hyejineee.flochallenge.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ConvertToSRTTests {

    @Test
    fun `가사 문자열 srt포맷으로 변환하기 테스트`() {
        val songInfo = SongInfo(
            duration = 198,
            lyrics = "[00:16:200]we wish you a merry christmas\n[00:18:300]we wish you a merry christmas"
        )
        assertThat(songInfo.convertLyricsStringToSRTString()).isEqualTo(
            "0\n" +
                    "00:00:16,200 --> 00:00:18,300\n" +
                    "we wish you a merry christmas\n" +
                    "\n" +
                    "1\n" +
                    "00:00:18,300 --> 00:03:18,000\n" +
                    "we wish you a merry christmas"
        )
    }

    @Test
    fun `srt문자열 가사 리스트로 바꾸기 테스트`() {
        val songInfo = SongInfo(
            duration = 198,
            lyrics = "0\n" +
                    "00:00:16,200 --> 00:00:18,300\n" +
                    "we wish you a merry christmas\n" +
                    "\n" +
                    "1\n" +
                    "00:00:18,300 --> 00:03:18,000\n" +
                    "we wish you a merry christmas"
        )

        assertThat(songInfo.convertSrtToLyricsList()).isEqualTo(
            listOf(
                Lyrics(0, 16200, 18300, "we wish you a merry christmas"),
                Lyrics(1, 18300, 198000, "we wish you a merry christmas")
            )
        )
    }

    @Test
    fun `srt시간 long으로 변환 테스트`() {
        assertThat(convertTimeStrToLong("00:00:16,200")).isEqualTo(16200)
    }

    @Test
    fun `long시간 string으로 변환 테스트`() {
        assertThat(convertLongTimeToString(16200)).isEqualTo("00:16")
        assertThat(convertLongTimeToString(198000)).isEqualTo("03:18")
    }

}