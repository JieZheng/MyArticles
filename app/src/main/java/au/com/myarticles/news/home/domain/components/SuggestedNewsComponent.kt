package au.com.myarticles.news.home.domain.components

import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.Component


data class SuggestedNewsComponent(
  val title: String,
  val abstract: String,
  val imageUrl: String = "",
  val url: String = "",
  val isStared: Boolean = false,
  val clickEvent: Any? = null,
  val longClickEvent: Any? = null,
  override var layout: Int = R.layout.component_suggested_news,
  override var sort: Long = 0,
  override var id: String = SuggestedNewsComponent::class.java.name,
  override var column: Int = 0
) : Component {

  override fun isSameContent(component : Component): Boolean {
    return component is SuggestedNewsComponent && this == component
  }

}