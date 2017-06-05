package au.com.myarticles.news.home.network

import au.com.myarticles.news.common.network.SerializerUtil
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

open class NewsListResponse {

  var newsList: List<NewsResponse> = mutableListOf()
}

open class NewsListResponseDeserializer : JsonDeserializer<NewsListResponse> {

  class NewsListResponseDeserializeException : Exception()

  override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): NewsListResponse {
    if (json.isJsonArray) {
      return NewsListResponse().apply {
        newsList = json.asJsonArray.map {
          SerializerUtil.gson.fromJson(it, NewsResponse::class.java)
        }
      }
    } else {
      throw NewsListResponseDeserializeException()
    }
  }
}