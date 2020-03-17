package com.hyejineee.flochallenge.model

import kotlin.time.*

data class Lyrics(
    val index: Int,
    val startTime: Long,
    val endTime: Long,
    val content: String
)

@UseExperimental(ExperimentalTime::class)
fun convertTimeStrToLong(time: String): Long {
    val times = time.split(":", ",").map { it.toInt() }
    val realTime = times[0].hours +
            times[1].minutes +
            times[2].seconds +
            times[3].milliseconds
    return realTime.toLongMilliseconds()
}

@UseExperimental(ExperimentalTime::class)
fun convertLongTimeToString(time: Long): String {
    val second = time.milliseconds.inSeconds
    return String.format("%02d:%02d", (second / 60).toInt(), (second % 60).toInt())
}
