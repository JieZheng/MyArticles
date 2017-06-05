package au.com.myarticles.news.home.domain

import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.Component
import au.com.myarticles.news.common.uiframework.presentation.components.WarningComponent
import au.com.myarticles.news.common.uiframework.support.ComponentProducer
import au.com.myarticles.news.home.data.News
import au.com.myarticles.news.home.data.NewsDataLayer
import au.com.myarticles.news.home.domain.components.NewsComponent
import au.com.myarticles.news.home.domain.events.NewsEvent
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

open class NewsListViewModel @Inject constructor() : ComponentProducer<NewsListViewModel.NewsListOptions>() {

  class ComponentOrder {
    companion object {
      val ORDER_ALL_NEWS: Long = 50
    }
  }

  enum class NewsDataSource {
    NETWORK,
    CACHE
  }

  data class NewsListOptions (
    val showSuggestedNews: Boolean = false,
    val dataSource: NewsDataSource = NewsDataSource.NETWORK
  )

  protected lateinit var newsDataLayer: NewsDataLayer
    @Inject set

  protected val allNewsSubscription = CompositeSubscription()

  private val refreshingBehaviour = BehaviorSubject.create<Boolean>(false)

  protected var allNewsBehaviour: BehaviorSubject<List<News>> = BehaviorSubject.create(listOf())

  protected val suggestedCategories = listOf( News.AUSTRALIA, News.GREAT_BRITAIN, News.RUSSIA, News.CHINA, News.SOUTH_KOREA)

  protected fun getNews(): Observable<List<News>> {
    return allNewsBehaviour
  }

  override fun createDisplayElements(startingObject: NewsListOptions): Observable<List<Component>> {
    return getNews().doOnSubscribe {
      refreshAllNews()
    }
      .doOnCompleted {
        refreshFinished()
      }
      .doOnError {
        refreshFinished()
      }
      .map { newsList ->
        var components = mutableListOf<Component>()
        components.addAll(getAllNewsComponents(newsList))
        components
      }
  }


  protected fun getAllNewsComponents(newsList: List<News>): List<Component> {
    return newsList.map { news ->
      NewsComponent(
        title = news.title,
        abstract = news.abstract,
        url = news.url,
        imageUrl = news.imageUrl,
        isStared = news.isStared,
        clickEvent = NewsEvent.ClickNews(news.url),
        longClickEvent = NewsEvent.LongClickNews(news),
        sort = ComponentOrder.ORDER_ALL_NEWS
      )
    }.let {
      if (it.isEmpty()) {
        listOf(
          WarningComponent(
          title = getString(R.string.no_articles),
          sort = ComponentOrder.ORDER_ALL_NEWS
        ))
      } else {
        it
      }
    }
  }

  override fun refresh(startingObject: NewsListOptions): Observable<Unit> {
    compositeSubscription.clear()

    return Observable.create<Unit> { statusSubscriber ->
      var isCompleted = false
      compositeSubscription.add(
        createDisplayElements(startingObject)
          .doOnEach {
            if (!isCompleted) {
              isCompleted = true
              logger.i(TAG_NAME, "finished refreshing")
              statusSubscriber.onCompleted()
            }
          }
          .subscribe(
            {
              behaviour.onNext(it)
            },
            {
              logger.w(TAG_NAME, "error refreshing card", it)
              emitError(it)
            }
          )
      )
    }
      .doOnSubscribe { refreshStarted() }
      .doOnTerminate { refreshFinished() }
      .doOnUnsubscribe { refreshFinished() }
  }

  fun refreshStarted() {
    refreshingBehaviour.onNext(true)
  }

  fun refreshFinished() {
    refreshingBehaviour.onNext(false)
  }

  override fun isRefreshing(startingObject: NewsListOptions): Observable<Boolean> = refreshingBehaviour.asObservable()

  fun refreshAllNews() {
    allNewsSubscription.clear()
    allNewsSubscription.add(
      newsDataLayer.getNewsList()
        .subscribe(
          {
            allNewsBehaviour.onNext(it)
          },
          {
            logger.w(TAG_NAME, " error get all news", it)
            emitError(it)
          }
        )
    )
  }

}