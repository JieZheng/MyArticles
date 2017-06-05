package au.com.myarticles.news.common.network

import com.google.gson.*
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SerializerUtil {

  companion object {
    val gson = Gson()
    fun toJson(obj: Any?): String {
      return gson.toJson(obj)
    }
  }

  val gsonBuilder: GsonBuilder

  @Inject constructor() {
    gsonBuilder = GsonBuilder()

  }

  open fun <T> fromJson(json: String, clazz: Class<T>): T {
    return gsonBuilder.create().fromJson(json, clazz)
  }

  open fun <T> fromJson(json: String, type: Type): T {
    return gsonBuilder.create().fromJson(json, type)
  }

  open fun toJson(obj: Any): String {
    return gsonBuilder.create().toJson(obj)
  }

  open fun toJson(obj: Any, type: Type): String {
    return gsonBuilder.create().toJson(obj)
  }

}
