package com.hyejineee.musicapp.mView

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.ByteArrayDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.hyejineee.musicapp.R
import com.hyejineee.musicapp.model.SongInfo

class PlayerService : Service() {
    private val binder = InnerBinder()
    private var player: SimpleExoPlayer? = null
    private lateinit var playerNotificationManager: PlayerNotificationManager

    inner class InnerBinder : Binder() {
        fun getService() = this@PlayerService
    }

    override fun onCreate() {
        super.onCreate()
        player = SimpleExoPlayer.Builder(this).build()
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            "PlaybackChannel",
            R.string.channel,
            1,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                override fun getCurrentContentText(player: Player): String? {
                    return ""
                }

                override fun getCurrentContentTitle(player: Player): String {
                    return "title"
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return null
                }

            }
        ).apply {
            setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()
                    playerNotificationManager.setPlayer(null)
                    player?.release()
                }

                override fun onNotificationStarted(
                    notificationId: Int,
                    notification: Notification
                ) {
                    startForeground(notificationId, notification)
                }
            })
        }

        playerNotificationManager.setPlayer(player)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (player == null) {
            player = SimpleExoPlayer.Builder(this).build()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun getPlayer() = player

    fun playerPrepare(songInfo: SongInfo) {
        player?.prepare(
            getSongSource(songInfo)
        )
    }

    private fun getSongSource(songInfo: SongInfo): MergingMediaSource {
        val defaultHttpDataSourceFactory =
            DefaultHttpDataSourceFactory(getString(R.string.app_name))
        val mediaSource = ProgressiveMediaSource
            .Factory(defaultHttpDataSourceFactory)
            .createMediaSource(Uri.parse(songInfo.file))

        val dataSourceFactory = DataSource.Factory {
            ByteArrayDataSource(songInfo.lyrics.toByteArray())
        }
        val subtitleFormat = Format.createTextSampleFormat(
            null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, null
        )

        val subtitleSource = SingleSampleMediaSource(
            Uri.parse(""), dataSourceFactory, subtitleFormat, C.TIME_UNSET
        )

        return MergingMediaSource(mediaSource, subtitleSource)
    }


}