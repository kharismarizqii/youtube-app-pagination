package com.kharismarizqii.youtubevideo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kharismarizqii.youtubevideo.R
import com.kharismarizqii.youtubevideo.model.playlist.PlaylistItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_playlist.view.*


class PlaylistAdapter: RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>(){

    private var list =  ArrayList<PlaylistItem>()

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: PlaylistItem){
            with(itemView){
                Picasso.get().load(item.snippet.thumbnails.high.url).fit().centerCrop().into(ivThumbPlaylist)
                tvTitle.text = item.snippet.title
                tvDesc.text = item.snippet.description

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(item) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        Log.d("Adapter", "onCreate: doApiCall: item ${position}")
        holder.bind(list[position])
    }

    fun addList(items: ArrayList<PlaylistItem>){
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PlaylistItem)
    }


}