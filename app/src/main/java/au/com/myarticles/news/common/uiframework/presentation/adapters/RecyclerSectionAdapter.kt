package au.com.myarticles.news.common.uiframework.presentation.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import java.util.*

abstract class RecyclerSectionAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  protected var items: ArrayList<RecyclerSectionItem>  = ArrayList()
  private var sections: List<RecyclerSection<RecyclerSectionItem>> = ArrayList()

  companion object {
    @JvmStatic val SECTION_HEADER: Int = 0
  }

  fun clear() {
    val numItems = items.size

    sections = emptyList()
    items.clear()

    if(numItems != 0) {
      notifyItemRangeRemoved(0, numItems)
    }
  }

  fun update(newSections: List<RecyclerSection<RecyclerSectionItem>>) {
    val oldSize = items.size

    sections = newSections
    buildItems()

    if(oldSize != 0) {
      notifyItemRangeRemoved(0, oldSize)
    }

    if(items.size != 0) {
      notifyItemRangeInserted(0, items.size)
    }
  }

  fun updateWithNoAnimations(newSections: List<RecyclerSection<RecyclerSectionItem>>) {
    sections = newSections
    buildItems()
    notifyDataSetChanged()
  }

  protected fun buildItems() {
    val newItems = ArrayList<RecyclerSectionItem>()
    for (section in sections) {
      if (section.showHeader()) {
        newItems.add(section)
      }

      newItems.addAll(section.getItems())
    }

    items.clear()
    items.addAll(newItems)

  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun getItemViewType(position: Int): Int {
    val item = items.get(position)
    if (item is RecyclerSection<*>) {
      if(item.showHeader()) {
        return SECTION_HEADER
      } else {
        return items[position].getType()
      }
    } else {
      return items[position].getType()
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
    val viewHolder = holder as? ViewHolder
    if (viewHolder != null) {
      if (viewHolder.type == SECTION_HEADER) {
        val section = items.get(position) as RecyclerSection<*>
        onBindHeader(holder, section)
      } else {
        val item: RecyclerSectionItem = items.get(position)
        onBindItem(holder, item)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
    return if (viewType == SECTION_HEADER) {
      onCreateHeader(parent) as RecyclerView.ViewHolder
    } else {
      onCreateItem(parent, viewType) as RecyclerView.ViewHolder
    }
  }

  interface ViewHolder {
    val type: Int
  }

  abstract fun onCreateHeader(parent: ViewGroup?): RecyclerSectionAdapter.ViewHolder
  abstract fun onCreateItem(parent: ViewGroup?, viewType: Int): RecyclerSectionAdapter.ViewHolder

  abstract fun onBindHeader(holder: RecyclerView.ViewHolder?, section: RecyclerSection<*>)
  abstract fun onBindItem(holder: RecyclerView.ViewHolder?, item: RecyclerSectionItem)

}
