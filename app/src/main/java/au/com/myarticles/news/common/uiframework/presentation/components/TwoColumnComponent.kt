package au.com.myarticles.news.common.uiframework.presentation.components

import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.Component

data class TwoColumnComponent(
  val items : List<Component>,
  override var layout: Int = R.layout.component_two_column,
  override var sort: Long = 0,
  override var id: String = TwoColumnComponent::class.java.name,
  override var column: Int = 0
) : Component {

  override fun isSameContent(component : Component): Boolean {
    return component is TwoColumnComponent && this == component
  }

}