package au.com.myarticles.news.common.network.services

import android.content.Context
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import au.com.myarticles.news.R
import au.com.myarticles.news.common.config.EnvironmentConfig
import au.com.myarticles.news.common.log.Logger
import au.com.myarticles.news.common.network.NetworkConnectivityUtil
import au.com.myarticles.news.common.network.NoNetworkConnectionException
import au.com.myarticles.news.common.network.SerializerUtil
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.RequestFuture
import rx.Observable
import rx.schedulers.Schedulers
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.ExecutionException
import javax.inject.Inject
import javax.inject.Singleton

val MAX_NUM_RETRIES = 0
val BACK_OFF_MULTIPLIER = 1f

@Singleton open class NetworkService @Inject constructor() {

  companion object {
    val APP_USER_AGENT = "MyApp-Agent"
  }

  lateinit var requestQueue: RequestQueue
    @Inject set

  lateinit var serializerUtil: SerializerUtil
    @Inject set

  lateinit var environmentConfig: EnvironmentConfig
    @Inject set

  lateinit var networkConnectivityUtil: NetworkConnectivityUtil
    @Inject set

  lateinit var logger: Logger
    @Inject set

  lateinit var packageInfo: PackageInfo
    @Inject set

  lateinit var context: Context
    @Inject set

  open public fun <T> jsonRequest(url: String,
                                  responseClass: Class<T>,
                                  serviceTag: String,
                                  method: Int = Request.Method.GET,
                                  payload: Map<String, String>? = null,
                                  headers: Map<String, String>? = null,
                                  body: String? = null,
                                  bodyContentType: String? = null): Observable<T> {
    return stringRequest(url, serviceTag, method, payload, headers, body, bodyContentType).
      map {
        serializerUtil.fromJson(it, responseClass)
      }
  }

  open fun stringRequest(
    url: String,
    serviceTag: String,
    method: Int = Request.Method.GET,
    payload: Map<String, String>? = null,
    headers: Map<String, String>? = null,
    body: String? = null,
    bodyContentType: String? = null
  ): Observable<String> {
    val future = RequestFuture.newFuture<String>()
    val requestURL =
      if (method == Request.Method.GET) {
        getRequestURLWithPayload(url, payload)
      } else {
        url
      }

    val request = object : NetworkServiceRequest(method, requestURL, future, future) {

      override fun getBody(): ByteArray? {
        return body?.toByteArray(
          if (Charset.isSupported(paramsEncoding)) {
            Charset.forName(paramsEncoding)
          } else {
            Charset.defaultCharset()
          }
        )
      }

      override fun getParams(): Map<String, String>? {
        return payload ?: super.getParams()
      }

      override fun getHeaders(): Map<String, String>? {
        val newHeaders = if (headers != null) {
          HashMap(headers)
        } else {
          HashMap<String, String>()
        }
        newHeaders.put(APP_USER_AGENT, getUserAgent())
        return newHeaders
      }

      override fun getBodyContentType(): String? {
        return bodyContentType ?: super.getBodyContentType()
      }

    }

    logger.d("NetworkService",
      "Request details for url=${url}, " +
        "method=${method}, " +
        "serviceTag=${serviceTag}, " +
        "headers=${request.headers}, " +
        "body=${body}, " +
        "payload=${payload}")

    return doRequest(future, request, serviceTag, url)
  }

  private fun getRequestURLWithPayload(url: String, payload: Map<String, String>?): String {
    if (payload != null) {
      val sb = StringBuilder()
      for ((key, value) in payload) {
        sb.append("${Uri.encode(key)}=${Uri.encode(value)}&")
      }
      return "${url}?${sb.toString()}"
    }
    return url
  }

  open fun doRequest(future: RequestFuture<String>,
                     request: NetworkServiceRequest,
                     serviceTag: String,
                     url: String): Observable<String> {
    if (!networkConnectivityUtil.isConnected) {
      logger.d("NetworkService", "Request not made, no internet connection url=${url}, serviceTag=${serviceTag}")
      return Observable.error(NoNetworkConnectionException())
    }

    request.setRetryPolicy(DefaultRetryPolicy(environmentConfig.networkTimeoutMillis, MAX_NUM_RETRIES, BACK_OFF_MULTIPLIER))
    request.setTag(serviceTag)
    request.setShouldCache(false)
    requestQueue.add(request)

    logger.d("NetworkService", "Request Started url=${url}, serviceTag=${serviceTag}, headers=${request.headers}, requestContentType=${request.bodyContentType}")

    return Observable.from(future, Schedulers.io())
      .doOnNext({
        logSuccessfulResponse(it, serviceTag, url)
      })
      .onErrorResumeNext({
        val wrappedException = it as? ExecutionException
        val error = wrappedException?.cause ?: it
        logErrorResponse(error, serviceTag, url)
        return@onErrorResumeNext Observable.error(error)
      })
      .subscribeOn(Schedulers.io())
  }

  private fun logErrorResponse(error: Throwable?, serviceTag: String, url: String) {
    val volleyError = error as? VolleyError
    val networkResponse = volleyError?.networkResponse
    val errorContent = if (networkResponse?.data != null) String(networkResponse!!.data) else null ?: error ?: "No reponse content"
    val details = "Request Error url=${url}, serviceTag=${serviceTag}, status= ${networkResponse?.statusCode}, response=${errorContent}"
    logger.w("NetworkService", details, error)
  }

  private fun logSuccessfulResponse(response: String, serviceTag: String, url: String) {
    logger.d("NetworkService", "Request Completed url=${url}, serviceTag=${serviceTag}, response=${response}")
  }

  open fun cancelRequests(serviceTag: String) {
    requestQueue.cancelAll(serviceTag)
  }

  fun getUserAgent(): String {
    val builder = StringBuilder()

    val displayMetrics = context.resources.displayMetrics

    builder.append("app")
    builder.append("/")
    builder.append(context.resources.getString(R.string.services_useragent))
    builder.append(" ")
    builder.append("v")
    builder.append(packageInfo.versionName)
    builder.append("/")
    builder.append(packageInfo.versionCode)
    builder.append("/")
    builder.append(Build.VERSION.RELEASE)
    builder.append(" ")
    builder.append("[")
    builder.append(Build.MANUFACTURER)
    builder.append(" - ")
    builder.append(Build.MODEL)
    builder.append(" ")
    builder.append("${(displayMetrics.widthPixels / displayMetrics.density).toInt()}x${(displayMetrics.heightPixels / displayMetrics.density).toInt()}")
    builder.append("]")

    return builder.toString()
  }
}
