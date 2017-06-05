package au.com.myarticles.news.common.network.services

import javax.inject.Inject
import kotlin.properties.Delegates

abstract class BaseService(val serviceTag: String) {

  var networkService: NetworkService by Delegates.notNull()
    @Inject set

  open fun cancelRequests() {
    networkService.cancelRequests(serviceTag)
  }
}
