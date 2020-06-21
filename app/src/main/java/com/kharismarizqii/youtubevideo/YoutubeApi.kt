package com.kharismarizqii.youtubevideo

class YoutubeApi{
    companion object{
        const val API_KEY: String = "AIzaSyBXNXJTKSrWehFTZOrJicD3C_shcJh-JdA"
        const val CHANNEL_ID = "UCyJa5OJG7GSrWJjizFBn32g"
        const val URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=$CHANNEL_ID&maxResults=20&key=$API_KEY"
        const val PLAYLIST_URL = "https://www.googleapis.com/youtube/v3/playlists?channelId=$CHANNEL_ID&key=$API_KEY"
    }
}
