package au.com.myarticles.news.home.data

import au.com.myarticles.news.common.data.RealmString
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class NewsRealmObject : RealmObject {

  constructor():super()

  constructor(news: News) {
    title = news.title
    abstract = news.abstract
    imageUrl = news.imageUrl
    url = news.url
    news.category.map {
      category.add(RealmString(it))
    }
  }

  @PrimaryKey
  var title: String = ""

  var abstract: String = ""

  var imageUrl: String = ""

  var url: String = ""

  var category: RealmList<RealmString> = RealmList()
}