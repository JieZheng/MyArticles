package au.com.myarticles.news.common.log

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class Logger @Inject constructor() {

  open fun d(tag: String, message: String, t: Throwable? = null) {
    Log.d(tag, message, t)
  }

  open fun e(tag: String, message: String, t: Throwable? = null) {
    Log.e(tag, message, t)
  }

  open fun w(tag: String, message: String, t: Throwable? = null) {
    Log.w(tag, message, t)
  }

  open fun i(tag: String, message: String, t: Throwable? = null) {
    Log.i(tag, message, t)
  }

  open fun wtf(tag: String, message: String, t: Throwable? = null) {
    Log.wtf(tag, message, t)
  }

  open fun getStackTraceString(thr: Throwable) : String {
    return Log.getStackTraceString(thr)
  }
}
