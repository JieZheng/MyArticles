package au.com.myarticles.news.home.data

import au.com.myarticles.news.common.log.Logger
import au.com.myarticles.news.common.network.services.ServiceManager
import au.com.myarticles.news.home.network.NewsService
import rx.Observable
import javax.inject.Inject

open class NewsDataLayer @Inject constructor(
  val serviceManager: ServiceManager,
  val service: NewsService,
  val cache: NewsCache,
  val logger: Logger
) {

  open fun getNewsList(): Observable<List<News>> {
    return serviceManager.getEndpoint(ServiceManager.KEY_NEWS_SOURCE_V2)
      .flatMap {
        service.getNews(it)
      }
      .map { newsResponseList ->
        newsResponseList.map(::News)
      }
  }

  open fun saveNews(obj: News): Observable<News> {
    obj.isStared = true
    return cache.save(NewsRealmObject(obj)).map(::News)
  }

  open fun deleteNews(obj: News) {
    obj.isStared = false
    cache.deleteNews(NewsRealmObject(obj))
  }

  open fun getCachedNewsList(): Observable<List<News>> {
    return cache.findAll().map {
      it.map(::News)
    }
  }

}