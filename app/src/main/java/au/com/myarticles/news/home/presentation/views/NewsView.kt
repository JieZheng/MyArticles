package au.com.myarticles.news.common.uiframework.presentation.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.ComponentPresenter
import au.com.myarticles.news.common.uiframework.FadeInComponentAnimator
import au.com.myarticles.news.home.domain.components.NewsComponent
import butterknife.bindView
import com.squareup.otto.Bus
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onLongClick

class NewsView : FrameLayout, ComponentPresenter<NewsComponent> {

  private val title: TextView by bindView(R.id.txt_title)
  private val abstract: TextView by bindView(R.id.txt_abstract)
  private val newsImage: NormalImageView by bindView(R.id.img_news_image)
  private val startImage: ImageView by bindView(R.id.img_star)

  constructor(ctx: Context) : super(ctx)
  constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

  override val itemAnimator = FadeInComponentAnimator(this)

  override fun apply(component: NewsComponent, bus: Bus) {
    super.apply(component, bus)

    title.text = component.title
    abstract.text = component.abstract
    newsImage.visibility = View.VISIBLE
    newsImage.loadImage(
      imageUrl = component.imageUrl,
      onError = {
        newsImage.visibility = View.GONE
      }
    )

    if (component.isStared) {
      startImage.visibility = View.VISIBLE
    } else {
      startImage.visibility = View.GONE
    }

    onClick {
      component.clickEvent?.let {
        bus.post(it)
      }
    }

    onLongClick {
      component.longClickEvent?.let {
        bus.post(it)
        true
      } ?: false
    }
  }

}