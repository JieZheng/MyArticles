package au.com.myarticles.news.common.uiframework

import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import android.view.animation.DecelerateInterpolator
import au.com.myarticles.news.common.uiframework.support.ComponentViewHolder
import au.com.myarticles.news.common.utils.*

open class FadeInComponentAnimator(val view: View) : ComponentAnimator {
  override fun cancelAnimation() {
    ViewCompat.animate(view).cancel()
  }

  override fun animateClear() {
    view.clearFadeIn()
  }

  override fun preAnimateAdd(holder: ComponentViewHolder) {
    view.fadeInPrepare()
  }

  override fun animateAdd(listener: ViewPropertyAnimatorListener, delay: Long) {
    view.fadeIn(listener, startDelay = delay)
  }

  override fun preAnimateRemove(holder: ComponentViewHolder) {
    view.fadeOutPrepare()
  }

  override fun animateRemove(listener: ViewPropertyAnimatorListener, delay: Long) {
    view.fadeOut(listener, startDelay = delay)
  }
}

class FadeInTranslateUpComponentAnimator(val view: View) : ComponentAnimator {
  override fun cancelAnimation() {
    ViewCompat.animate(view).cancel()
  }

  override fun animateClear() {
    view.clearFadeInTranslateUp()
  }

  override fun preAnimateAdd(holder: ComponentViewHolder) {
    view.fadeInTranslateUpPrepare()
  }

  override fun animateAdd(listener: ViewPropertyAnimatorListener, delay: Long) {
    view.fadeInTranslateUp(listener, startDelay = delay, interpolator = DecelerateInterpolator())
  }

  override fun preAnimateRemove(holder: ComponentViewHolder) {
    view.fadeOutTranslateDownPrepare()
  }

  override fun animateRemove(listener: ViewPropertyAnimatorListener, delay: Long) {
    view.fadeOutTranslateDownPrepare(listener, startDelay = delay, interpolator = DecelerateInterpolator())
  }
}

class FadeInScaleInComponentAnimator(val view: View) : ComponentAnimator {
  override fun cancelAnimation() {
    ViewCompat.animate(view).cancel()
  }

  override fun animateClear() {
    view.clearScaleInFadeIn()
  }

  override fun preAnimateAdd(holder: ComponentViewHolder) {
    view.scaleInFadeInPrepare()
  }

  override fun animateAdd(listener: ViewPropertyAnimatorListener, delay: Long) {
    view.scaleInFadeIn(listener, startDelay = delay)
  }

  override fun preAnimateRemove(holder: ComponentViewHolder) {
    view.scaleOutFadeOutPrepare()
  }

  override fun animateRemove(listener: ViewPropertyAnimatorListener, delay: Long) {
    view.scaleOutFadeOut(listener, startDelay = delay)
  }
}

open class NoAnimationComponentAnimator(val view: View) : ComponentAnimator {
  override fun animateClear() {

  }

  override fun cancelAnimation() {

  }

  override fun animateAdd(listener: ViewPropertyAnimatorListener, delay: Long) {
    //no default animation, inform recycler that animation has finished
    listener.onAnimationEnd(view)
  }

  override fun animateRemove(listener: ViewPropertyAnimatorListener, delay: Long) {
    //no default animation, inform recycler that animation has finished
    listener.onAnimationEnd(view)
  }

  override fun preAnimateRemove(holder: ComponentViewHolder) {
  }

  override fun preAnimateAdd(holder: ComponentViewHolder) {
  }
}