package au.com.myarticles.news.common.uiframework.support

import android.view.View
import android.view.ViewGroup
import au.com.myarticles.news.common.uiframework.Component
import au.com.myarticles.news.common.uiframework.ComponentPresenter
import au.com.myarticles.news.common.uiframework.presentation.adapters.BaseSimpleListAdapter
import com.squareup.otto.Bus
import org.jetbrains.anko.layoutInflater

class ComponentListAdapter(val bus: Bus, var animateOnFirstLoad: Boolean = false) :
  BaseSimpleListAdapter<Component, ComponentViewHolder>(
    Component::class,
    ComponentComparator,
    isSameCallback,
    sameContentCallback
  ) {

  companion object {
    private val isSameCallback = { item1: Component, item2: Component -> item1.isSameDisplayElement(item2) }

    private val sameContentCallback = { item1: Component, item2: Component -> item1.isSameContent(item2) }
  }

  override fun getItemViewType(position: Int): Int {
    return data.get(position).layout
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
    val v: View = parent.context.layoutInflater.inflate(viewType, parent, false)
    return ComponentViewHolder(v, viewType, this)
  }

  @Suppress("UNCHECKED_CAST")
  override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
    val displayElement = data.get(position)
    val presenter = holder.itemView as ComponentPresenter<Component>
    presenter.apply(displayElement, bus)
  }

  var currentElementIds: List<String> = emptyList()

  fun setAndAnimate(elements: List<Component>) {
    val sortedElements = elements.sortedBy { it }
    val newElementIds = sortedElements.map { it.id }
    val currentElements = getAll()

    if (!animateOnFirstLoad) {
      animateOnFirstLoad = true
      set(elements)
    } else {
      currentElements.forEach {
        if (!newElementIds.contains(it.id)) {
          remove(it)
        }
      }

      if (elements.isNotEmpty()) {
        addAll(elements)
      }

      currentElementIds = newElementIds.minus(currentElements.map { it.id })
    }
  }

}

