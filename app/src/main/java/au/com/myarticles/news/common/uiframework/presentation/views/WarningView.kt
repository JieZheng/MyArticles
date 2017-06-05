package au.com.myarticles.news.common.uiframework.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.ComponentPresenter
import au.com.myarticles.news.common.uiframework.FadeInComponentAnimator
import au.com.myarticles.news.common.uiframework.presentation.components.WarningComponent
import butterknife.bindView
import com.squareup.otto.Bus
import org.jetbrains.anko.onClick

class WarningView : FrameLayout, ComponentPresenter<WarningComponent> {

  private val title: TextView by bindView(R.id.txt_title)

  constructor(ctx: Context) : super(ctx)
  constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

  override val itemAnimator = FadeInComponentAnimator(this)

  override fun apply(component: WarningComponent, bus: Bus) {
    super.apply(component, bus)

    title.text = component.title

    onClick {
      component.clickEvent?.let{
        bus.post(it)
      }
    }
  }

}