package au.com.myarticles.news.home.network

import au.com.myarticles.news.common.log.Logger
import au.com.myarticles.news.common.network.services.BaseService
import au.com.myarticles.news.common.network.services.Endpoint
import com.android.volley.Request
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class NewsService @Inject constructor(val logger: Logger) : BaseService(NewsService.javaClass.simpleName) {

  companion object {
    private val TAG = NewsService::class.java.simpleName
  }

  open fun getNews(endpoint: Endpoint): Observable<List<NewsResponse>> {
    return networkService.jsonRequest(
      url = endpoint.url,
      responseClass = NewsListResponse::class.java,
      serviceTag = serviceTag,
      method = Request.Method.GET,
      payload = null,
      headers = null,
      body = null,
      bodyContentType = "application/json"
    ).doOnError { error ->
      logger.e(TAG, "Failed request for news list. $error")
    }.map {
      it.newsList
    }
  }
}
