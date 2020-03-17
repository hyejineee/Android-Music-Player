package com.hyejineee.flochallenge.mView

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.upstream.ByteArrayDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.hyejineee.flochallenge.R
import com.hyejineee.flochallenge.databinding.ActivityMainBinding
import com.hyejineee.flochallenge.mVIewModel.SongInfoViewModel
import com.hyejineee.flochallenge.model.SongInfo
import com.hyejineee.flochallenge.model.convertLongTimeToString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), BaseInit {

    private val songInfoViewModel: SongInfoViewModel by viewModel()
    private var player: SimpleExoPlayer? = null
    private lateinit var viewDataBinding: ActivityMainBinding
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        player = SimpleExoPlayer.Builder(this).build()
        viewDataBinding = setContentView(this, R.layout.activity_main)

        initView()
        initDataBinding()
        initSubscribe()

        setPlayerSubtitleChangeListener()
        setPlayerProgressBarChangeListener()

        getSongInfo()
    }

    override fun initView() {
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.playerView.player = player
        viewDataBinding.playerView.showTimeoutMs = 0
    }

    override fun initDataBinding() {
        viewDataBinding.activity = this
        viewDataBinding.songInfoViewModel = songInfoViewModel
    }

    override fun initSubscribe() {
        songInfoViewModel.songInfoSubject
            .subscribe({
                viewDataBinding.playerView.findViewById<TextView>(R.id.end_timebar_text)
                    .text = convertLongTimeToString(songInfoViewModel.lyricsList.last().endTime)
                setLyricsTextView(it.title, it.singer)
                viewDataBinding.songInfo = it
                player?.prepare(
                    getSongSource(it)
                )
            }, {
                Log.e("error", it.message)
            }).addTo(compositeDisposable)

        songInfoViewModel.currentLyricsIndexSubject
            .subscribe {
                setLyricsTextView(
                    songInfoViewModel.lyricsList[it].content,
                    if (it == songInfoViewModel.lyricsList.lastIndex) ""
                    else songInfoViewModel.lyricsList[it + 1].content
                )
            }
            .addTo(compositeDisposable)
    }

    fun showLyricsDialog() {
        val dialog = LyricsDialog(player)
        if (!dialog.isResumed)
            dialog.show(supportFragmentManager, "")
    }

    private fun getSongInfo() {
        songInfoViewModel.getSongInfo()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { songInfoViewModel.songInfo = it }
            .addTo(compositeDisposable)
    }

    private fun setLyricsTextView(vararg texts: String) {
        viewDataBinding.currentLyricsText.text = texts[0]
        viewDataBinding.nextLyricsText.text = texts[1]
    }

    private fun setPlayerSubtitleChangeListener() {
        player?.addTextOutput {
            if (player?.isPlaying == false) return@addTextOutput
            val currentTime = player?.currentPosition ?: 0
            songInfoViewModel.setPosition(currentTime)
        }
    }

    private fun setPlayerProgressBarChangeListener() {
        val timeText = viewDataBinding.playerView.findViewById<TextView>(R.id.timebar_text)
        viewDataBinding.playerView.setProgressUpdateListener { position, _ ->
            timeText.text = convertLongTimeToString(position)
            if (player?.isPlaying == false) return@setProgressUpdateListener
            songInfoViewModel.setPosition(position)
        }
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
