package com.kharismarizqii.youtubevideo.model.playlist

data class PlaylistResponse(
    val etag: String,
    val items: ArrayList<PlaylistItem>,
    val kind: String,
    val nextPageToken: String?,
    val prevPageToken: String?,
    val pageInfo: PlaylistPageInfo
)