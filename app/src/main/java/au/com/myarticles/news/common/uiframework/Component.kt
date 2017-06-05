package au.com.myarticles.news.common.uiframework

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes

interface Component : Comparable<Component> {
  var sort: Long /* var because of deserialisation */
  var id: String /* var because of deserialisation */
  var column: Int /* var because of deserialisation */

  @get:IdRes val viewId: Int? get() = null
  @get:LayoutRes val layout: Int

  override fun compareTo(other: Component): Int {
    return if (sort.compareTo(other.sort) != 0) {
      sort.compareTo(other.sort)
    } else {
      id.compareTo(other.id)
    }
  }

  fun isSameContent(component : Component): Boolean

  fun isSameDisplayElement(other: Component): Boolean {
    return id == other.id
  }
}