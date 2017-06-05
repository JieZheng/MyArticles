package au.com.myarticles.news.home.network

import com.google.gson.annotations.SerializedName

open class NewsResponse {

  @SerializedName("title")
  var title: String = ""

  @SerializedName("abstract")
  var abstract: String = ""

  @SerializedName("image_url")
  var imageUrl: String = ""

  @SerializedName("url")
  var url: String = ""

  @SerializedName("category")
  var category: List<String> = emptyList()
}