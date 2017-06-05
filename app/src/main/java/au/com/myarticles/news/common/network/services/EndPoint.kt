package au.com.myarticles.news.common.network.services

open class Endpoint {
  constructor(url: String, version: String = "") {
    this.url = url
    this.version = version
  }

  open var url: String = ""
  open var version: String = ""
}