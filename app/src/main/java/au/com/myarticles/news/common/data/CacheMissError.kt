package au.com.myarticles.news.common.data

public open class CacheMissError(clazz: Class<out Any>, query: String? = null) : Error("Object Not Found In Cache for class: ${clazz} with query: ${query}") {}

