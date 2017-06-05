package au.com.myarticles.news.common.uiframework

import android.support.v4.view.ViewPropertyAnimatorListener
import au.com.myarticles.news.common.uiframework.support.ComponentViewHolder

interface ComponentAnimator {
  fun animateClear()
  fun cancelAnimation()
  fun animateAdd(listener: ViewPropertyAnimatorListener, delay: Long)
  fun animateRemove(listener: ViewPropertyAnimatorListener, delay: Long)
  fun preAnimateRemove(holder: ComponentViewHolder)
  fun preAnimateAdd(holder: ComponentViewHolder)
  fun enableChangeAnimation() = false
}