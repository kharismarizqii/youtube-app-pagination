package com.kharismarizqii.youtubevideo.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    var page = 1
    var isLoading = false
    var limit = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        list = ArrayList()
        val linearLayout = LinearLayoutManager(this)
        rvPlaylist.setHasFixedSize(true)
        rvPlaylist.layoutManager = linearLayout
        adapter = PlaylistAdapter()
        rvPlaylist.adapter = adapter
        doApiCall(null)

        rvPlaylist.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    val visibleItemCount = linearLayout.childCount
                    val pastVisibleItem = linearLayout.findFirstVisibleItemPosition()
                    val total = adapter.itemCount

                    if (!isLoading ){
                        if (visibleItemCount + pastVisibleItem >= total){
                            doApiCall(nextPage)
                        }
                    }
                }
            }
        })
    }

    private fun doApiCall(page: String?) {
        isLoading = true
        progressBar.visibility = View.VISIBLE
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
            }
        })
    }

    override fun onRefresh() {
//        itemCount = 0;
//        currentPage = PAGE_START;
//        isLastPage = false;
//        adapter.clear();
//        list.clear()
//        doApiCall(null);
    }
}
