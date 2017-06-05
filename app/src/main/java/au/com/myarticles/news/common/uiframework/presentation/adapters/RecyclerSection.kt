package au.com.myarticles.news.common.uiframework.presentation.adapters


interface RecyclerSection<T : RecyclerSectionItem> : RecyclerSectionItem {
  fun getTitle(): String
  fun getItems(): List<T>
  fun showHeader(): Boolean

  override fun getType(): Int {
    return 0
  }
}