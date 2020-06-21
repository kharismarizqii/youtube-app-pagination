package com.kharismarizqii.youtubevideo.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.kharismarizqii.youtubevideo.R
import com.kharismarizqii.youtubevideo.YoutubeApi

class DetailVideoActivity : YouTubeBaseActivity(){

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESCRIPTION = "extra_desc"
        const val EXTRA_PUBLISH = "extra_publish"
    }

    private lateinit var onFullscreen: YouTubePlayer.OnFullscreenListener
    private lateinit var onInitializedListener: YouTubePlayer.OnInitializedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)
        val id = intent.getStringExtra(EXTRA_ID)
        val fm = fragmentManager
        var player: YouTubePlayerFragment? =
            fm.findFragmentByTag(YouTubePlayerFragment::class.java.simpleName) as YouTubePlayerFragment?
        if (player == null) {
            val ft = fm.beginTransaction()
            player = YouTubePlayerFragment.newInstance()
            ft.add(android.R.id.content, player, YouTubePlayerFragment::class.java.simpleName)
            ft.commit()
        }

        player?.initialize(YoutubeApi.API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.d(MainActivity.TAG, "onClick: Done Initializing Youtube Player")
                val link = "https://youtu.be/sx9XMjgoPgo"
                val linkGenerate = link.replace("https://youtu.be/", "")
                if (!p2) {
                    p1?.loadVideo(id)
                    p1?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    p1?.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)
                    p1?.setFullscreen(true)
                    p1?.play()
//                    p1?.cueVideo(id)
                }

//                p1.loadVideo("https://www.youtube.com/playlist?list=PLMFoVGj1AtY-XPjCIWwcEfhXi9CHLpjzG")
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.d(MainActivity.TAG, "onClick: Fail to initialize")
            }

        })
    }

//        btnPlay.setOnClickListener {
//            ytPlayerView.initialize(YoutubeApi.API_KEY, onInitializedListener)
//        }

}

