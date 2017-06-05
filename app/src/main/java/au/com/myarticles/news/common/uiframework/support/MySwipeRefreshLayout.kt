package au.com.myarticles.news.common.uiframework.support

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import au.com.myarticles.news.R


class MySwipeRefreshLayout : SwipeRefreshLayout {

  constructor(context: Context):super(context){}
  constructor(context: Context, attrs: AttributeSet):super(context,attrs){}

  private var canRefresh: Boolean = true
  private var appbarExtended: Boolean = true

  private var measured = false;
  private var preMeasureRefreshing = false

  override fun onFinishInflate() {
    super.onFinishInflate()
    this.setColorSchemeResources(R.color.raw_light_blue)
  }
  
  fun setCanRefresh(enabled: Boolean) {
    canRefresh = enabled
    isEnabled = appbarExtended && canRefresh
  }

  fun getCanRefresh():Boolean{
    return canRefresh
  }

  override fun onMeasure(widthMeasureSpec:Int, heightMeasureSpec:Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    if (!measured) {
      measured = true
      isRefreshing = preMeasureRefreshing
    }
  }

  override fun setRefreshing(refreshing:Boolean) {
    if (measured) {
      super.setRefreshing(refreshing)
    } else {
      preMeasureRefreshing = refreshing
    }
  }

}