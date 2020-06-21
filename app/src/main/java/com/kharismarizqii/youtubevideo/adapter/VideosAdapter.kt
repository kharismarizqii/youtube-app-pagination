package com.kharismarizqii.youtubevideo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kharismarizqii.youtubevideo.R
import com.kharismarizqii.youtubevideo.model.playlist.PlaylistItem
import com.kharismarizqii.youtubevideo.model.videos.VideosItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_playlist.view.*

class VideosAdapter: RecyclerView.Adapter<VideosAdapter.VideosViewHolder>(){

    private var list = ArrayList<VideosItem>()

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class VideosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: VideosItem){
            with(itemView){
                tvTitle.text = item.snippet.title
                tvDesc.text = item.snippet.description
                Picasso.get().load(item.snippet.thumbnails.high.url).fit().centerCrop().into(ivThumbPlaylist)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return VideosViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addList(items: ArrayList<VideosItem>){
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: VideosItem)
    }
}