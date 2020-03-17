package com.hyejineee.flochallenge.mView

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hyejineee.flochallenge.R
import com.hyejineee.flochallenge.databinding.LyricsDialogBinding
import com.hyejineee.flochallenge.mVIewModel.SongInfoViewModel
import com.hyejineee.flochallenge.model.convertLongTimeToString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LyricsDialog(private val player: SimpleExoPlayer?) : BottomSheetDialogFragment(), BaseInit {

    private lateinit var viewDataBinding: LyricsDialogBinding
    private val songInfoViewModel: SongInfoViewModel by sharedViewModel()
    var selectLyricsMode: Boolean = false
    val compositeDisposable = CompositeDisposable()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        viewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.lyrics_dialog,
            null,
            false
        )
        bottomSheet.setContentView(viewDataBinding.root)
        BottomSheetBehavior
            .from((viewDataBinding.root.parent as View))
            .state = BottomSheetBehavior.STATE_EXPANDED

        val param = viewDataBinding.lyricsDialogContainer.layoutParams
        param.height = ((Resources.getSystem().displayMetrics.heightPixels) * 1.0).toInt()
        viewDataBinding.lyricsDialogContainer.layoutParams = param

        initView()
        initDataBinding()
        initSubscribe()

        return bottomSheet
    }

    override fun initView() {
        viewDataBinding.playerView.player = player
        viewDataBinding.playerView.showTimeoutMs = 0
        viewDataBinding.lyricsList.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.lyricsList.adapter =
            LyricsAdapter(
                songInfoViewModel.lyricsList,
                object : ItemClickListener {
                    override fun onClick(position: Long) {
                        if (!selectLyricsMode) {
                            dismiss()
                            return
                        }
                        player?.seekTo(position)
                    }
                }
            )

        val timeText = viewDataBinding.playerView.findViewById<TextView>(R.id.timebar_text)
        viewDataBinding.playerView.findViewById<TextView>(R.id.end_timebar_text)
            .text = convertLongTimeToString(
            songInfoViewModel.lyricsList.last().endTime
        )

        player?.addTextOutput {
            if (player?.isPlaying == false) return@addTextOutput
            val currentTime = player?.currentPosition ?: 0
            songInfoViewModel.setPosition(currentTime)
        }

        viewDataBinding.playerView.setProgressUpdateListener { position, _ ->
            timeText.text = convertLongTimeToString(position)
            if (player?.isPlaying == false) return@setProgressUpdateListener
            songInfoViewModel.setPosition(position)
        }
    }

    override fun initDataBinding() {
        viewDataBinding.mView = this
        viewDataBinding.songInfoViewModel = songInfoViewModel
        viewDataBinding.songInfo = songInfoViewModel.songInfo
    }

    override fun initSubscribe() {
        songInfoViewModel.currentLyricsIndexSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewDataBinding.lyricsList.scrollToPosition(it)
                (viewDataBinding.lyricsList.adapter as LyricsAdapter).apply {
                    currentPosition = it
                    notifyDataSetChanged()
                }
            }, {
            })
            .addTo(compositeDisposable)
    }

    fun changeMode() {
        selectLyricsMode = !selectLyricsMode
        viewDataBinding.touchMode = selectLyricsMode
    }

    fun closeDialog() {
        dismiss()
    }
}
