package au.com.myarticles.news.common.network

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.OkUrlFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class OkHttpStack(okHttpClient: OkHttpClient) : MyHurlStack() {

  private val factory: OkUrlFactory

  init {
    factory = OkUrlFactory(okHttpClient)
  }

  @Throws(IOException::class) override fun createConnection(url: URL): HttpURLConnection {
    return factory.open(url)
  }

}
