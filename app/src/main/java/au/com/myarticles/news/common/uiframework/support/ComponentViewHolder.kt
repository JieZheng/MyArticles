package au.com.myarticles.news.common.uiframework.support

import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.ComponentPresenter
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder

class ComponentViewHolder(itemView: View, val type: Int, val adapterLevel: ComponentListAdapter) : AnimateViewHolder(itemView) {

  val staggeredDelay = itemView.resources.getInteger(R.integer.card_appearance_staggered_delay).toLong()

  override fun animateRemoveImpl(listener: ViewPropertyAnimatorListener) {
    (itemView as ComponentPresenter<*>).itemAnimator.animateRemove(object : ViewPropertyAnimatorListener {
      override fun onAnimationEnd(view: View?) {
        listener.onAnimationEnd(itemView)
      }

      override fun onAnimationCancel(view: View?) {
        listener.onAnimationCancel(itemView)
      }

      override fun onAnimationStart(view: View?) {
        listener.onAnimationStart(itemView)
      }

    }, getDelay())
  }

  override fun preAnimateRemoveImpl() {
    (itemView as ComponentPresenter<*>).itemAnimator.preAnimateRemove(this)
  }

  override fun animateAddImpl(listener: ViewPropertyAnimatorListener) {
    (itemView as ComponentPresenter<*>).itemAnimator.animateAdd(object : ViewPropertyAnimatorListener {
      override fun onAnimationEnd(view: View?) {
        listener.onAnimationEnd(itemView)
      }

      override fun onAnimationCancel(view: View?) {
        listener.onAnimationCancel(itemView)
      }

      override fun onAnimationStart(view: View?) {
        listener.onAnimationStart(itemView)
      }

    }, getDelay())
  }

  override fun preAnimateAddImpl() {
    (itemView as ComponentPresenter<*>).itemAnimator.preAnimateAdd(this)
  }

  private fun getDelay(): Long {
    return if (this.adapterPosition != -1) {
      val id = adapterLevel.get(this.adapterPosition).id
      val idx = adapterLevel.currentElementIds.indexOf(id)
      if (idx != -1) idx * staggeredDelay else 0
    } else {
      0
    }
  }

}