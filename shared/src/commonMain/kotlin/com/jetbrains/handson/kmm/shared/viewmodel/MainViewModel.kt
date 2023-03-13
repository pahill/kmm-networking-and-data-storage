package com.jetbrains.handson.kmm.shared.viewmodel

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.rickclephas.kmm.viewmodel.KMMViewModel

open class MainViewModel() : KMMViewModel() {
    fun getMoreContent(rocketLaunch: RocketLaunch): MoreContent {
        return when {
            !rocketLaunch.links.youtubeLink.isNullOrBlank()  && !rocketLaunch.links.youtubeId.isNullOrBlank()-> {
                MoreContent.YoutubeContent(rocketLaunch.links.youtubeLink, rocketLaunch.links.youtubeId)
            }
            !rocketLaunch.links.wikipediaLink.isNullOrBlank() -> {
                MoreContent.WikipediaContent(rocketLaunch.links.wikipediaLink)
            }
            !rocketLaunch.links.articleLink.isNullOrBlank() -> {
                MoreContent.ArticleContent(rocketLaunch.links.articleLink)
            }
            else -> {
                MoreContent.NoContent
            }
        }
    }
}

sealed class MoreContent() {
    class YoutubeContent(val link: String, val id: String) : MoreContent()
    class WikipediaContent(val link: String) : MoreContent()
    class ArticleContent(val link: String) : MoreContent()
    object NoContent : MoreContent()
}