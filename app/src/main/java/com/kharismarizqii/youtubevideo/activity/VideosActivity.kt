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
import com.kharismarizqii.youtubevideo.adapter.VideosAdapter
import com.kharismarizqii.youtubevideo.api.RetrofitClient
import com.kharismarizqii.youtubevideo.model.videos.VideosItem
import com.kharismarizqii.youtubevideo.model.videos.VideosResponse
import kotlinx.android.synthetic.main.activity_videos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class VideosActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object{
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESCRIPTION = "extra_desc"
        const val EXTRA_PUBLISH = "extra_publish"
    }

    private lateinit var adapter: VideosAdapter
    private var nextPage = ""
    var isLoading = false
    private lateinit var id: String
    private lateinit var videoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        id = intent.getStringExtra(EXTRA_ID)!!
        val title = intent.getStringExtra(EXTRA_TITLE)
        val desc = intent.getStringExtra(EXTRA_DESCRIPTION)
        val publish = intent.getStringExtra(EXTRA_PUBLISH)
        val date = formatDate(publish!!)
        val textDate = "Published: $date"
        tvTitlePlaylist.text = title
        tvPublish.text = textDate
        tvDesc.text = desc

        val layoutManager = LinearLayoutManager(this)
        swipeRefresh.setOnRefreshListener(this)
        rvVideos.setHasFixedSize(true)
        rvVideos.layoutManager = layoutManager
        adapter = VideosAdapter()
        rvVideos.adapter = adapter
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
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    val total = adapter.itemCount
                    if (!isLoading && !nextPage.isNullOrEmpty()) {
                        if (visibleItemCount + pastVisibleItem >= total) {
                            doApiCall(nextPage, false)
                        }
                    }
                }
            }

        })
    }

    private fun doApiCall(page: String?, isOnRefresh: Boolean) {
        isLoading = true
        if(!isOnRefresh) progressBar.visibility = View.VISIBLE
        val parameters = HashMap<String, String>()
        Log.d("ID", "id: $id")
        parameters["playlistId"] = id
        parameters["key"] = YoutubeApi.API_KEY
        parameters["part"]= "contentDetails, snippet"
        if (page!=null) parameters["pageToken"] = page
        RetrofitClient.instance.getVideos(parameters).enqueue(object: Callback<VideosResponse>{
            override fun onFailure(call: Call<VideosResponse>, t: Throwable) {
                Log.e("ID", "error: ${t.message}")
                Toast.makeText(this@VideosActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<VideosResponse>,
                response: Response<VideosResponse>
            ) {
                nextPage = response.body()?.nextPageToken.toString()
                Log.d("ID", "nextPage: $nextPage")
                val listResponse = response.body()?.items
                if (listResponse!=null){
                    Log.d("ID", "listResponse: $listResponse")
                    adapter.addList(listResponse)
                }
                progressBar.visibility = View.GONE
                isLoading = false
                swipeRefresh.isRefreshing = false
                showVideos()
            }

        })
    }

    private fun showVideos() {
        adapter.setOnItemClickCallback(object : VideosAdapter.OnItemClickCallback{
            override fun onItemClicked(data: VideosItem) {
                Intent(this@VideosActivity, DetailVideoActivity::class.java).also {
                    it.putExtra(DetailVideoActivity.EXTRA_ID, data.contentDetails.videoId)
                    startActivity(it)
                }
            }

        })
    }

    private fun formatDate(publish: String): String {

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.US)
        val output = formatter.format(parser.parse(publish)!!)
        return output
    }

    override fun onRefresh() {
        adapter.clear()
        doApiCall(null, true)
    }
}
