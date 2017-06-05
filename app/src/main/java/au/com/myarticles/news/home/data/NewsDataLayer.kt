package au.com.myarticles.news.home.data

import au.com.myarticles.news.common.log.Logger
import au.com.myarticles.news.common.network.services.ServiceManager
import au.com.myarticles.news.home.network.NewsService
import rx.Observable
import javax.inject.Inject

open class NewsDataLayer @Inject constructor(
  val serviceManager: ServiceManager,
  val service: NewsService,
  val logger: Logger
) {

  open fun getNewsList(): Observable<List<News>> {
    return serviceManager.getEndpoint(ServiceManager.KEY_NEWS_SOURCE_V1)
      .flatMap {
        service.getNews(it)
      }
      .map { newsResponseList ->
        newsResponseList.map(::News)
      }
  }

}