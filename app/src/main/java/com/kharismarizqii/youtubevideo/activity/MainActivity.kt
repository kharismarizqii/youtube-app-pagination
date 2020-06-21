package com.kharismarizqii.youtubevideo.activity

import android.os.Bundle
import android.util.Log
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.kharismarizqii.youtubevideo.R
import com.kharismarizqii.youtubevideo.YoutubeApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : YouTubeBaseActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    private lateinit var onInitializedListener : YouTubePlayer.OnInitializedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: Starting")
        onInitializedListener = object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
                Log.d(TAG, "onClick: Done Initializing Youtube Player")
                val link = "https://youtu.be/sx9XMjgoPgo"
                val linkGenerate = link.replace("https://youtu.be/", "")
                p1?.loadVideo(linkGenerate)
//                p1.loadVideo("https://www.youtube.com/playlist?list=PLMFoVGj1AtY-XPjCIWwcEfhXi9CHLpjzG")
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Log.d(TAG, "onClick: Fail to initialize")
            }

        }

        btnPlay.setOnClickListener {
            Log.d(TAG, "onClick: Initializing Youtube Player")
            ytPlayerView.initialize(YoutubeApi.API_KEY, onInitializedListener)
        }

        ytPlayerView.setOnClickListener {
            Log.d(TAG, "onClick: Initializing Youtube Player")
            ytPlayerView.initialize(YoutubeApi.API_KEY, onInitializedListener)
        }

    }
}
