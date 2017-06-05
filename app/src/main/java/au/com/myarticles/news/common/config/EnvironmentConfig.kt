package au.com.myarticles.news.common.config

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnvironmentConfig @Inject constructor() {
  companion object {
    private val NETWORK_TIMEOUT_MILLIS_DEFAULT = 60 * 1000
    private val SSL_ENABLED = false
    private val BASE_URL = "api.myjson.com"
  }

  val networkTimeoutMillis: Int = NETWORK_TIMEOUT_MILLIS_DEFAULT

  fun isSslEnabled(): Boolean {
    return SSL_ENABLED
  }

  fun getBaseUrl(): String {
    return BASE_URL
  }
}
