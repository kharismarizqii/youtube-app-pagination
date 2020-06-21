package com.kharismarizqii.youtubevideo.model.videos

data class VideosResponse(
    val etag: String,
    val items: ArrayList<VideosItem>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo
)