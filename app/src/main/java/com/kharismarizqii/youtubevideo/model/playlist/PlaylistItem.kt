package com.kharismarizqii.youtubevideo.model.playlist

data class PlaylistItem(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: PlaylistSnippet
)