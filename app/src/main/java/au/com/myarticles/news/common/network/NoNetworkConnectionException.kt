package au.com.myarticles.news.common.network


class NoNetworkConnectionException : RuntimeException {
  constructor(detailMessage: String? = null, throwable: Throwable? = null) : super(detailMessage, throwable)
}
