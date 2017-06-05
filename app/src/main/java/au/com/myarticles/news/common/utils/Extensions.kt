package au.com.myarticles.news.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager
import android.widget.TextView
import au.com.myarticles.news.NewsApp
import au.com.myarticles.news.di.AppComponent
import com.squareup.otto.Bus
import org.json.JSONObject
import rx.Observable
import java.io.InputStream
import java.util.*
import kotlin.reflect.KClass

fun <T> List<T>.insertBetween(item: T): List<T> {
  val list = ArrayList<T>()
  this.forEachIndexed { i, element ->
    if (i > 0 && i < this.size) {
      list.add(item)
    }
    list.add(element)
  }
  return list
}

fun String?.toIntOrNull(): Int? {
  try {
    return Integer.parseInt(this)
  } catch(e: NumberFormatException) {
    return null
  }
}

fun <T> Boolean.choose(satisfied: T, unsatisfied: T): T {
  return if (this) {
    satisfied
  } else {
    unsatisfied
  }
}

fun CharSequence?.isNullOrBlank(): Boolean {
  return this == null || this.isBlank()
}

fun View.getAppComponent(): AppComponent {
  return NewsApp.getAppComponent()
}

fun Fragment.getAppComponent(): AppComponent {
  return NewsApp.getAppComponent()
}

fun Context.getBoolean(id: Int): Boolean {
  return resources.getBoolean(id)
}

@Suppress("DEPRECATION")
fun Context.getColorCompat(@ColorRes id: Int): Int {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    return getColor(id)
  } else {
    return resources.getColor(id)
  }
}

@Suppress("DEPRECATION")
fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    return getDrawable(id)
  } else {
    return resources.getDrawable(id)
  }
}

fun View.getParentInnerWidth(): Int {
  return (parent as View).getMeasuredWidth() - (parent as View).paddingEnd - (parent as View).paddingStart
}

inline fun <reified T> HashMap<String, Any>.get(key: String, defaultValue: T): T {
  return if (containsKey(key)) {
    get(key) as T
  } else {
    defaultValue
  }
}

inline fun <reified T> HashMap<String, Any>.getWithNull(key: String, defaultValue: T? = null): T? {
  return if (containsKey(key)) {
    get(key) as T?
  } else {
    defaultValue
  }
}


/**
 * Setting background on CardView will hide cardview even though it is clickable
 */
@SuppressLint("NewApi")
fun View.setSelectableBackground(): Drawable {
  val backgroundTypedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
  val selectableItemBackground = backgroundTypedArray.getDrawable(0)
  backgroundTypedArray.recycle()
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    foreground = selectableItemBackground
  } else {
    background = selectableItemBackground
  }
  return selectableItemBackground
}

fun TextView.setTextFromStringOrInt(obj: Any?) {
  if (obj is Int) {
    this.text = resources.getString(obj)
  } else if (obj is String) {
    this.text = obj
  } else {
    this.text = obj.toString()
  }
}

fun copyToClipboard(context: Context, label: String, value: String) {
  val data = ClipData.newPlainText(label, value)
  val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  clipboard.primaryClip = data
}

fun Resources.getStatusBarHeight(): Int {
  val resourceId = getIdentifier("status_bar_height", "dimen", "android");
  if (resourceId > 0) {
    return getDimensionPixelSize(resourceId)
  }
  return 0
}

fun Any?.nullSafeEquals(that: Any?): Boolean {
  if (this == null && that == null) return true
  if (this == null && that != null) return false
  if (this != null && that == null) return false
  return this == that
}

fun Drawable.toGrayScale() {
  val matrix = ColorMatrix()
  matrix.setSaturation(0f)
  this.colorFilter = ColorMatrixColorFilter(matrix)
}

/**
 * Convert Dps to pixels for use in drawing calculations
 */
fun dpsToPx(dp: Float, context: Context): Float {
  val r = context.resources
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
}

fun pxToDps(px: Float, context: Context): Float {
  val r = context.resources
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, r.displayMetrics)
}

fun readFile(context: Context, fileName: String): InputStream {
  return context.assets.open(fileName)
}

fun hideKeyboard(context: Context, view: View?) {
  if (view != null) {
    val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    im.hideSoftInputFromWindow(view.windowToken, 0)
  }
}

fun showKeyboard(context: Context, view: View?) {
  if (view != null) {
    val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    im.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
  }
}

fun hideKeyboard(context: Context, windowToken: IBinder) {
  val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  im.hideSoftInputFromWindow(windowToken, 0)
}

fun JSONObject.getOrNull(name: String): Any? {
  return if (has(name)) {
    get(name)
  } else {
    null
  }
}

fun <T> Observable<T>.onErrorReturnNull(exceptionClass: KClass<out Throwable> = Throwable::class): Observable<T?> {
  return this
    .map { it }
    .onErrorResumeNext {
      if (exceptionClass.java.isAssignableFrom(it.javaClass)) {
        Observable.just(null)
      } else {
        Observable.error(it)
      }
    }
}

fun <T> Observable<T>.onErrorReturnNothing(exceptionClass: KClass<out Throwable> = Throwable::class): Observable<T> {
  return this
    .onErrorResumeNext {
      if (exceptionClass.java.isAssignableFrom(it.javaClass)) {
        Observable.empty()
      } else {
        Observable.error(it)
      }
    }
}

fun <T> Observable<T?>.filterNulls(): Observable<T> {
  return this
    .filter { it != null }
    .map { it as T }
}

fun <T, R> List<T>.flatMapNullable(transform: (T) -> Iterable<R>?): List<R> {
  return this.flatMap { transform.invoke(it) ?: emptyList<R>() }
}

fun Collection<*>?.isEmptyOrNull(): Boolean {
  return this == null || this.isEmpty()
}

fun LinearLayoutManager.fixTopScrollingAfterInsertingElement() {
  //Note: fix scrolling issue with first card being hidden when inserted
  if (findFirstCompletelyVisibleItemPosition() == 0) {
    scrollToPosition(0)
  }
}

fun Drawable.tint(@ColorInt tintColor: Int): Drawable {
  val wrappedDrawable = DrawableCompat.wrap(this).mutate()
  DrawableCompat.setTint(wrappedDrawable, tintColor)
  return wrappedDrawable
}

@Suppress("DEPRECATION")
fun TextView.setTextAppearanceCompat(@StyleRes style: Int) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    setTextAppearance(style)
  } else {
    setTextAppearance(context, style)
  }
}

class HtmlCompatUtil {
  companion object {
    @Suppress("DEPRECATION")
    fun fromHtml(source: String): Spanned {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
      }
      return Html.fromHtml(source)
    }
  }
}

fun Activity.findContentView() = findViewById(android.R.id.content)

@Suppress("DEPRECATION", "UNUSED")
fun CookieManager.removeAllCookiesCompat() {
  val handler = Handler(Looper.getMainLooper())
  handler.post {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      CookieManager.getInstance().removeAllCookies(null)
    } else {
      CookieManager.getInstance().removeAllCookie()
    }
  }
}

fun View.observeOnViewEvent(bus: Bus?, event: Any?) {
  if (bus != null && event != null) {
    var onScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
    var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    fun checkVisibleStateAndPostEvent() {
      var visibleRect = Rect()
      val isVisible = getLocalVisibleRect(visibleRect)
      if (isVisible
        && visibleRect.left == 0
        && visibleRect.right == width && visibleRect.height() == height) {

        bus.post(event)
        viewTreeObserver.removeOnScrollChangedListener(onScrollChangedListener)
        viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
      }
    }

    onScrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
      checkVisibleStateAndPostEvent()
    }
    viewTreeObserver.addOnScrollChangedListener(onScrollChangedListener)

    onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
      checkVisibleStateAndPostEvent()
    }
    viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
  }
}