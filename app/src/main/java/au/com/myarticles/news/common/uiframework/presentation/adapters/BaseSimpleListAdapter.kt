package au.com.myarticles.news.common.uiframework.presentation.adapters

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.util.SortedListAdapterCallback
import java.util.*
import kotlin.reflect.KClass

abstract class BaseSimpleListAdapter<T : Any, V : RecyclerView.ViewHolder>(
  clazz: KClass<T>,
  val sorter: Comparator<T>? = null,
  val sameCallBack: ((T, T) -> Boolean)? = null,
  val areContentsTheSameCallback: ((T, T) -> Boolean)? = null) : RecyclerView.Adapter<V>() {

  val data: SortedList<T> = SortedList(clazz.java, object : SortedListAdapterCallback<T>(this) {
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
      return if (areContentsTheSameCallback != null) {
        areContentsTheSameCallback.invoke(oldItem, newItem)
      } else {
        oldItem == newItem
      }
    }

    override fun areItemsTheSame(item1: T, item2: T): Boolean {
      return if (sameCallBack != null) {
        sameCallBack.invoke(item1, item2)
      } else {
        item1 === item2
      }
    }

    override fun compare(o1: T, o2: T): Int {
      return if (sorter != null) {
        sorter.compare(o1, o2)
      } else {
        0
      }
    }
  });

  fun getAll(): List<T> {
    return (0..data.size() - 1)
      .map { data.get(it) }
      .map { it }
  }

  fun addAll(items: List<T>) {
    data.addAll(items)
  }

  fun remove(item: T) {
    data.remove(item)
  }

  fun set(items: List<T>) {
    data.clear()
    data.addAll(items)
    notifyDataSetChanged()
  }

  fun add(item: T) {
    data.add(item)
  }

  override fun getItemCount(): Int {
    return data.size()
  }

  fun get(index: Int): T {
    return data.get(index)
  }

}