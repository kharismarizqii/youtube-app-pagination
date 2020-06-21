package com.kharismarizqii.youtubevideo.model.videos

data class VideosItem(
    val contentDetails: ContentDetails,
    val etag: String,
    val kind: String,
    val snippet: Snippet
)