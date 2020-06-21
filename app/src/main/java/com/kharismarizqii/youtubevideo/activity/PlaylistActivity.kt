package com.kharismarizqii.youtubevideo.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kharismarizqii.youtubevideo.R
import com.kharismarizqii.youtubevideo.YoutubeApi
import com.kharismarizqii.youtubevideo.adapter.PlaylistAdapter
import com.kharismarizqii.youtubevideo.api.RetrofitClient
import com.kharismarizqii.youtubevideo.model.playlist.PlaylistItem
import com.kharismarizqii.youtubevideo.model.playlist.PlaylistResponse
import kotlinx.android.synthetic.main.activity_playlist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val TAG = "PlaylistActivity"
    private lateinit var list: ArrayList<PlaylistItem>
    private lateinit var adapter: PlaylistAdapter
    private var nextPage = ""
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        list = ArrayList()
        val linearLayout = LinearLayoutManager(this)
        swipeRefresh.setOnRefreshListener(this)
        rvPlaylist.setHasFixedSize(true)
        rvPlaylist.layoutManager = linearLayout
        adapter = PlaylistAdapter()
        rvPlaylist.adapter = adapter
        doApiCall(null, false)
        nestedScrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY>0){
                    val visibleItemCount = linearLayout.childCount
                    val pastVisibleItem = linearLayout.findFirstVisibleItemPosition()
                    val total = adapter.itemCount
                    if (!isLoading && !nextPage.isNullOrEmpty() ){
                        if (visibleItemCount + pastVisibleItem >= total){
                            doApiCall(nextPage, false)
                        }
                    }
                }
            }
        })
    }

    private fun doApiCall(page: String?, isOnRefresh: Boolean) {
        isLoading = true
        Log.d(TAG, "doApiCall : page $page")
        if (!isOnRefresh) progressBar.visibility = View.VISIBLE
        val parameters = HashMap<String, String>()
        parameters["channelId"] = YoutubeApi.CHANNEL_ID
        parameters["key"] = YoutubeApi.API_KEY
        parameters["part"] = "snippet"
        if (page!= null) parameters["pageToken"] = page
        RetrofitClient.instance.getPlaylist(parameters).enqueue(object: Callback<PlaylistResponse>{
            override fun onFailure(call: Call<PlaylistResponse>, t: Throwable) {
                Toast.makeText(this@PlaylistActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<PlaylistResponse>,
                response: Response<PlaylistResponse>
            ) {
                nextPage = response.body()?.nextPageToken.toString()
                val listResponse = response.body()?.items
                if (listResponse != null) {
                    list.clear()
                    list.addAll(listResponse)
                    adapter.addList(list)

                }
                progressBar.visibility = View.GONE
                isLoading = false
                swipeRefresh.isRefreshing = false
                showSelectedPlaylist()
            }
        })
    }

    private fun showSelectedPlaylist() {
        adapter.setOnItemClickCallback(object : PlaylistAdapter.OnItemClickCallback{
            override fun onItemClicked(data: PlaylistItem) {
                Intent(this@PlaylistActivity, VideosActivity::class.java).also {
                    it.putExtra(VideosActivity.EXTRA_ID, data.id)
                    it.putExtra(VideosActivity.EXTRA_TITLE, data.snippet.title)
                    it.putExtra(VideosActivity.EXTRA_DESCRIPTION, data.snippet.description)
                    it.putExtra(VideosActivity.EXTRA_PUBLISH, data.snippet.publishedAt)
                    startActivity(it)
                }
            }
        })
    }

    override fun onRefresh() {
        adapter.clear();
        list.clear()
        doApiCall(null, true);
    }
}
