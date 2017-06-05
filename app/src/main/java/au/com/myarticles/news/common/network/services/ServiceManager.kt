package au.com.myarticles.news.common.network.services

import au.com.myarticles.news.common.log.Logger
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ServiceManager @Inject constructor(val logger: Logger) {

  companion object {
    val TAG = ServiceManager::class.java.simpleName
    val KEY_NEWS_SOURCE_V1 = "news_source_v1"
    val KEY_NEWS_SOURCE_V2 = "news_source_v2"
  }

  open fun getEndpoint(endpointName: String): Observable<Endpoint> {
    return when(endpointName) {
      KEY_NEWS_SOURCE_V1 -> Observable.just(
        Endpoint("https://api.myjson.com/bins/51mx1")
      )
      KEY_NEWS_SOURCE_V2 -> Observable.just(
        Endpoint("https://cdn.rawgit.com/moskvax/5169afeb755f48c621c7a0fe86759da1/raw/dee726efdffdb5c033cceba1f68113d7d26b9731/world-20170531.json")
      )
      else -> {
        val exception = NoEndPointException()
        logger.e(TAG, "Cannot find the endpoint for:$endpointName", exception)
        Observable.error(exception)
      }
    }
  }
}
