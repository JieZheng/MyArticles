package au.com.myarticles.news.common.uiframework.presentation.components

import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.Component

data class HeaderComponent(
  val title: String,
  val clickEvent: Any? = null,
  override var layout: Int = R.layout.component_header,
  override var sort: Long = 0,
  override var id: String = HeaderComponent::class.java.name,
  override var column: Int = 0
) : Component {

  override fun isSameContent(component : Component): Boolean {
    return component is HeaderComponent && this == component
  }

}