package au.com.myarticles.news.home.presentation

import android.content.Intent
import android.net.Uri
import au.com.myarticles.news.NewsApp
import au.com.myarticles.news.common.uiframework.support.ComponentProducer
import au.com.myarticles.news.common.uiframework.support.GenericComponentFragment
import au.com.myarticles.news.home.domain.NewsListViewModel
import au.com.myarticles.news.home.domain.NewsListViewModel.NewsListOptions
import au.com.myarticles.news.home.domain.NewsListViewModel.NewsDataSource
import au.com.myarticles.news.home.domain.events.NewsEvent
import com.squareup.otto.Subscribe
import rx.Observable
import javax.inject.Inject

class BookmarkedNewsFragment : GenericComponentFragment<NewsListOptions>() {

  protected lateinit var newsListViewModel : NewsListViewModel
    @Inject set

  companion object {
    val TAG = BookmarkedNewsFragment::class.java.simpleName
    val Fragment_Bookmarked_News: String = "Fragment_Bookmarked_News"

    fun create(): BookmarkedNewsFragment {
      val fragment = BookmarkedNewsFragment()
      return fragment
    }
  }

  override fun inject() {
    NewsApp.getCurrentActivityComponent().inject(this)
  }

  override fun getViewModels(): List<ComponentProducer<NewsListOptions>> {
    return listOf(
      newsListViewModel
    )
  }

  override fun getStartingObject(): Observable<NewsListOptions> {
    return Observable.just(NewsListOptions(
      showSuggestedNews = false,
      dataSource = NewsDataSource.CACHE
    ))
  }

  override fun canRefresh(): Observable<Boolean> {
    return Observable.just(true)
  }

  @Suppress("UNUSED")
  @Subscribe
  fun onClickNews(e: NewsEvent.ClickNews) {
    if (!e.url.isEmpty()) {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      intent.data = Uri.parse(e.url)
      startActivity(intent)
    }
  }

}