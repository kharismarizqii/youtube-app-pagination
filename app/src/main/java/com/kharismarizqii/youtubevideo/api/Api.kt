package com.kharismarizqii.youtubevideo.api

import com.kharismarizqii.youtubevideo.model.playlist.PlaylistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {
    @GET("playlists")
    fun getPlaylist(
        @QueryMap parameters: HashMap<String, String>
    ): Call<PlaylistResponse>
}