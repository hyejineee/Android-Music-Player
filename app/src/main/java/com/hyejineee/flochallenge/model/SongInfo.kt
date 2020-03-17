package com.hyejineee.flochallenge.model

import kotlin.time.ExperimentalTime

data class SongInfo(
    val singer: String = "",
    val album: String = "",
    val title: String = "",
    val duration: Int = -1,
    val image: String = "",
    val file: String = "",
    var lyrics: String = ""
)

@UseExperimental(ExperimentalTime::class)
fun SongInfo.convertLyricsStringToSRTString(): String {
    val line = lyrics.split("\n")
    return line.foldIndexed(StringBuffer()) { index: Int, acc: StringBuffer, s: String ->
        when (index) {
            line.lastIndex -> {
                acc.append(line.lastIndex)
                acc.append("\n")

                acc.append(convertLyricsTimeToSrtTime(line.last().substringBefore("]").drop(1)))
                acc.append(" --> ")
                acc.append(String.format("%02d:%02d:%02d,000", 0, duration / 60, duration % 60))
                acc.append("\n")

                acc.append(line.last().substringAfter("]"))
            }
            else -> {
                acc.append(index)
                acc.append("\n")

                acc.append(convertLyricsTimeToSrtTime(s.substringBefore("]").drop(1)))
                acc.append(" --> ")
                acc.append(convertLyricsTimeToSrtTime(line[index + 1].substringBefore("]").drop(1)))
                acc.append("\n")

                acc.append(s.substringAfter("]"))
                acc.append("\n")
                acc.append("\n")
            }
        }
        acc
    }.toString()
}

fun SongInfo.convertSrtToLyricsList(): List<Lyrics> =
    lyrics.split("\n\n").map {
        val contents = it.split("\n")
        val time = contents[1].split(" --> ")
        Lyrics(
            contents[0].toInt(),
            convertTimeStrToLong(time[0]),
            convertTimeStrToLong(time[1]),
            contents[2]
        )
    }

fun convertLyricsTimeToSrtTime(timeStr: String): String {
    val splitStr = timeStr.split(":")
    return "00:${splitStr[0]}:${splitStr[1]},${splitStr[2]}"
}
