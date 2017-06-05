package au.com.myarticles.news.common.uiframework.support

import au.com.myarticles.news.common.uiframework.Component
import java.util.*

object ComponentComparator : Comparator<Component> {
  override fun compare(lhs: Component, rhs: Component): Int {
    return lhs.compareTo(rhs)
  }
}