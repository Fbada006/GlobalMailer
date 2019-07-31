package com.droidafricana.globalmail.domain

data class Article(
        val articleTitle: String,
        val articleDescription: String?,
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?,
        var isLiked: Boolean
)