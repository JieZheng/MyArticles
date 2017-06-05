package au.com.myarticles.news.common.uiframework

import android.view.View
import android.view.ViewGroup

fun View.setOptionalId(viewId: Int?, defaultViewId: Int) {
  if (viewId != null) {
    setId(viewId)
  } else {
    setId(defaultViewId)
  }
}

fun View.setLeftMargin(leftMargin: Int) {
  val lp = layoutParams as ViewGroup.MarginLayoutParams
  lp.leftMargin = leftMargin
  layoutParams = lp
}

fun View.setRightMargin(rightMargin: Int) {
  val lp = layoutParams as ViewGroup.MarginLayoutParams
  lp.rightMargin = rightMargin
  layoutParams = lp
}

fun View.setBottomMargin(bottomMargin: Int) {
  val lp = layoutParams as ViewGroup.MarginLayoutParams
  lp.bottomMargin = bottomMargin
  layoutParams = lp
}

fun View.setTopMargin(topMargin: Int) {
  val lp = layoutParams as ViewGroup.MarginLayoutParams
  lp.topMargin = topMargin
  layoutParams = lp
}
