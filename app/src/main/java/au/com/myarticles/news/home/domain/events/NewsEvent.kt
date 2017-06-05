package au.com.myarticles.news.home.domain.events

import au.com.myarticles.news.home.data.News

class NewsEvent {
  data class ClickNews(val url: String = "")
  data class LongClickNews(val news: News)
  object OpenSuggestedNewsPage
}