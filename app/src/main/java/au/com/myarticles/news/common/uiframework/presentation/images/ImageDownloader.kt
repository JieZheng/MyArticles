package au.com.myarticles.news.common.uiframework.presentation.images

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.DrawableTypeRequest
import com.bumptech.glide.GenericRequestBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.StreamEncoder
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.load.resource.file.FileToStreamDecoder
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.caverock.androidsvg.SVG
import java.io.InputStream
import java.util.*

class ImageDownloader {

  companion object {
    private val missingImages: MutableList<String> = Collections.synchronizedList(LinkedList())

    fun addMissingImage(url: String) {
      missingImages.add(url)
    }

    fun hasMissingImage(url: String): Boolean {
      return missingImages.contains(url)
    }

    fun loadImage(context: Context, url: String): DrawableRequestBuilder<String> {
      return DrawableRequestBuilder(context, url)
    }

    fun loadImage(context: Context, @DrawableRes resId: Int): DrawableRequestBuilder<Int> {
      return DrawableRequestBuilder(context, resId)
    }

    fun loadSvg(context: Context, url: String): SVGRequestBuilder {
      return SVGRequestBuilder(context, url)
    }

  }

  interface Callback {
    fun onSuccess()
    fun onError()
  }

  class DrawableRequestBuilder<T> {

    private val request: DrawableTypeRequest<T>

    constructor(context: Context, data: T) {
      request = Glide.with(context).load(data)
    }

    fun placeholder(@DrawableRes resId: Int): DrawableRequestBuilder<T> {
      request.placeholder(resId)
      return this
    }

    fun error(@DrawableRes resId: Int): DrawableRequestBuilder<T> {
      request.error(resId)
      return this
    }

    fun centerCrop(): DrawableRequestBuilder<T> {
      request.centerCrop()
      return this
    }

    fun into(imageView: ImageView, callback: Callback? = null) {
      callback?.let {
        request.listener(object : RequestListener<T, GlideDrawable> {
          override fun onResourceReady(resource: GlideDrawable?, model: T?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
            it.onSuccess()
            return false
          }

          override fun onException(e: Exception?, model: T?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
            Log.e(ImageDownloader::class.java.simpleName, "error downloading image resource", e)
            it.onError()
            return false
          }
        })
      }

      request.into(imageView)
    }

  }

  // Note: this was built using the sample from Glide here: https://github.com/bumptech/glide/tree/v3.6.0/samples/svg
  class SVGRequestBuilder {

    private val request: GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable>

    constructor(context: Context, url: String) {
      request = Glide.with(context)
        .using(Glide.buildStreamModelLoader(Uri::class.java, context), InputStream::class.java)
        .from(Uri::class.java)
        .`as`(SVG::class.java)
        .transcode(SVGDrawableTranscoder(), PictureDrawable::class.java)
        .sourceEncoder(StreamEncoder())
        .cacheDecoder(FileToStreamDecoder<SVG>(SVGDecoder()))
        .decoder(SVGDecoder())
        .listener(SvgLoaderListener<Uri>())
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .load(Uri.parse(url))
    }

    fun into(imageView: ImageView) {
      request.into(imageView)
    }
  }

}

