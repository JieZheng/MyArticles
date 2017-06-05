package au.com.myarticles.news.common.uiframework.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.Component
import au.com.myarticles.news.common.uiframework.ComponentPresenter
import au.com.myarticles.news.common.uiframework.FadeInComponentAnimator
import au.com.myarticles.news.common.uiframework.presentation.components.TwoColumnComponent
import butterknife.bindView
import com.squareup.otto.Bus

class TwoColumnView : FrameLayout, ComponentPresenter<TwoColumnComponent> {

  private val column1: LinearLayout by bindView(R.id.linear_layout_column1)
  private val column2: LinearLayout by bindView(R.id.linear_layout_column2)

  constructor(ctx: Context) : super(ctx)
  constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

  override val itemAnimator = FadeInComponentAnimator(this)

  override fun apply(component: TwoColumnComponent, bus: Bus) {
    super.apply(component, bus)

    column1.removeAllViews()
    column2.removeAllViews()
    component.items.forEachIndexed { index, component ->
      val itemView = View.inflate(context, component.layout, null) as ComponentPresenter<Component>
      itemView.apply(component, bus)

      if (isFirstColumn(index)) {
        column1.addView(itemView as View)
      } else {
        column2.addView(itemView as View)
      }
    }
  }

  private fun isFirstColumn(index: Int): Boolean = (index % 2 == 0)

}