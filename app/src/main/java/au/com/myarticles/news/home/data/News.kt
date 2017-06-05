package au.com.myarticles.news.home.data

import au.com.myarticles.news.home.network.NewsResponse

open class News {

  companion object {
    val AUSTRALIA = "Australia"
    val GREAT_BRITAIN = "Great Britain"
    val RUSSIA = "Russia"
    val CHINA = "China"
    val SOUTH_KOREA = "South Korea"
  }

  constructor():super()

  constructor(newsResponse: NewsResponse) {
    title = newsResponse.title
    abstract = newsResponse.abstract
    imageUrl = newsResponse.imageUrl
    url = newsResponse.url
    newsResponse.category.map {
      category.add(it)
    }
  }

  var title: String = ""

  var abstract: String = ""

  var imageUrl: String = ""

  var url: String = ""

  var category: ArrayList<String> = arrayListOf()

  var isStared: Boolean = false
}