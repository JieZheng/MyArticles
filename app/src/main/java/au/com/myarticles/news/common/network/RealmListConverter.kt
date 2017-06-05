package au.com.myarticles.news.common.network

import com.google.gson.JsonElement
import com.google.gson.JsonArray
import io.realm.RealmList
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import io.realm.RealmObject
import java.lang.reflect.Type


class RealmListConverter<T : RealmObject>(private val mElementType: Type) : JsonSerializer<RealmList<T>>, JsonDeserializer<RealmList<T>> {

  override fun serialize(src: RealmList<T>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
    val jsonArray = JsonArray()
    for (t in src) {
      jsonArray.add(context.serialize(t))
    }
    return jsonArray
  }

  @Throws(JsonParseException::class)
  override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RealmList<T> {
    val tags = RealmList<T>()
    val jsonArray = json.asJsonArray
    for (jsonElement in jsonArray) {
      tags.add(context.deserialize(jsonElement, mElementType))
    }
    return tags
  }
}