package au.com.myarticles.news.home.data

import au.com.myarticles.news.common.data.BaseCache
import rx.Observable
import javax.inject.Inject

open class NewsCache @Inject constructor(): BaseCache<NewsRealmObject>(NewsRealmObject::class.java) {
  companion object {
    private val NEWS_TITLE_LABEL = "title"
  }
  open fun deleteNews(obj: NewsRealmObject) {
    deleteAnyExistingWithProperty(NEWS_TITLE_LABEL, obj.title)
  }

}