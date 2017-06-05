package au.com.myarticles.news.common.uiframework

import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Bus
import org.jetbrains.anko.layoutInflater

interface ComponentPresenter<T : Component> {
  val itemAnimator: ComponentAnimator
  fun apply(component: T, bus: Bus) {
    (this as View).setOptionalId(component.viewId, View.NO_ID)
  }

  //TODO move this somewhere else
  @Suppress("UNCHECKED_CAST")
  fun inflateElementsIntoContainer(components: List<Component>, container: ViewGroup, bus: Bus): List<View> {
    return components.map { component ->
      val id = component.layout
      val v = container.context.layoutInflater.inflate(id, container, false)
      (v as ComponentPresenter<Component>).apply(component, bus)
      v
    }
  }

  fun getDefaultAnimator(view: View): ComponentAnimator {
    return NoAnimationComponentAnimator(view)
  }
}