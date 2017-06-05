package au.com.myarticles.news.common.uiframework.presentation.images

import android.graphics.drawable.PictureDrawable
import android.os.Build
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target

class SvgLoaderListener<T>: RequestListener<T, PictureDrawable> {

  override fun onResourceReady(resource: PictureDrawable, model: T, target: Target<PictureDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
    setLayerType(target)
    return false
  }

  override fun onException(e: Exception, model: T, target: Target<PictureDrawable>, isFirstResource: Boolean): Boolean {
    Log.e(SvgLoaderListener::class.java.simpleName, "Error trying to load svg", e)
    setLayerType(target)
    return false
  }

  private fun setLayerType(target: Target<PictureDrawable>) {
    val imageView = (target as ImageViewTarget<*>).view
    if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
      imageView.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
    }
  }

}

