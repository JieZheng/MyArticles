package au.com.myarticles.news.common.uiframework.presentation.views

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.presentation.images.ImageDownloader

open class DownloadableImageView : ImageView {

  var path: String? = null
  var onSuccessCallback: (() -> Unit)? = null
  var onErrorCallback: (() -> Unit)? = null
  var errorImage: Int? = null

  companion object {
    @JvmStatic val SMALL_PIXELS = 750
    @JvmStatic val MEDIUM_PIXELS = 1080
    @JvmStatic val LARGE_PIXELS = 2048
  }

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

  protected fun setCustomImagePath(path: String, onSuccess: (() -> Unit)? = null,
                                   @DrawableRes placeHolderImage: Int?,
                                   @DrawableRes errorImage: Int?,
                                   onError: (() -> Unit)? = null,
                                   forceRefresh: Boolean = false) {
    val callback = object : ImageDownloader.Callback {
      override fun onError() {
        ImageDownloader.addMissingImage(path)
        onError?.invoke()
      }

      override fun onSuccess() {
        onSuccess?.invoke()
      }
    }

    if (!forceRefresh && ImageDownloader.hasMissingImage(path)) {
      if (errorImage != null) {
        ImageDownloader.loadImage(context, errorImage)
          .centerCrop()
          .into(this, callback)
      } else {
        onError?.invoke()
      }
      return
    }

    val builder = ImageDownloader.loadImage(context, "$path")
    placeHolderImage?.let {
      builder.placeholder(it)
    }
    errorImage?.let {
      builder.error(it)
    }
    builder.centerCrop().into(this, callback)
  }

  // for phone use screen width
  // for tablet use larger of height/width as rotation possible
  protected fun getPixelsForDevice(): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay

    val metrics = DisplayMetrics()
    display.getMetrics(metrics)
    val pixels = metrics.widthPixels

    val isTablet = context.resources.getBoolean(R.bool.hero_image_is_tablet)

    if (isTablet) {
      return Math.max(pixels, metrics.heightPixels)
    }
    return pixels
  }

}