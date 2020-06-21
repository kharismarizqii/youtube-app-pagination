package com.kharismarizqii.youtubevideo.model.playlist

data class PlaylistSnippet(
    val publishedAt: String,
    val title: String,
    val description: String,
    val thumbnails: PlaylistThumbnail
)