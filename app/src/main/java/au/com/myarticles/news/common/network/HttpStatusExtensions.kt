@file:JvmName("HttpStatusExtensions")
package au.com.myarticles.news.common.network

val VALID_HTTP_CODES = 200..299

fun String.isValidHttpResponseCode(): Boolean {
  try {
    return this.toInt().isValidHttpResponseCode()
  } catch(e: NumberFormatException) {
    return false
  }
}

fun Int.isValidHttpResponseCode(): Boolean {
  return VALID_HTTP_CODES.any { it.equals(this) }
}