package com.hyejineee.musicApp.mView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hyejineee.musicApp.databinding.LyricsItemBinding
import com.hyejineee.musicApp.model.Lyrics

class LyricsAdapter(
    private val lyricsList: List<Lyrics>,
    private val clickListener: ItemClickListener
) : RecyclerView.Adapter<LyricsAdapter.mViewHolder>() {
    var currentPosition = 0

    inner class mViewHolder(val itemBinding: LyricsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(lyrics: Lyrics) {
            itemBinding.lyrics = lyrics
            itemBinding.root.setOnClickListener {
                clickListener.onClick(lyrics.startTime)
            }
            itemBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        mViewHolder(LyricsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = lyricsList.size

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val lyricsItem = lyricsList[position]

        holder.itemBinding.isCurrent = position == currentPosition
        holder.bind(lyricsItem)
    }
}