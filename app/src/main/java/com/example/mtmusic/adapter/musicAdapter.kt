package com.example.mtmusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mtmusic.R
import com.example.mtmusic.bean.musicInfo

class musicAdapter(musicData: MutableList<musicInfo>, listener: OnItemClickListener) : RecyclerView.Adapter<musicAdapter.ViewHolder>() {

    private var musicData = mutableListOf<musicInfo>()
    private var listener: OnItemClickListener? = null

    init {
        this.musicData = musicData
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false)
        return ViewHolder(view, listener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val musicInfo = musicData[position]
        holder.title!!.text = musicInfo.title
    }

    override fun getItemCount() = musicData.size

    class ViewHolder(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }

        var title: TextView? = null

        init {
            title = view.findViewById(R.id.title)
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}