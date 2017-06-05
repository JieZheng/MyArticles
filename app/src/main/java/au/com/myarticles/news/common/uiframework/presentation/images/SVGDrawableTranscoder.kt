package au.com.myarticles.news.common.uiframework.presentation.images

import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.caverock.androidsvg.SVG

class SVGDrawableTranscoder: ResourceTranscoder<SVG, PictureDrawable> {

  override fun transcode(toTranscode: Resource<SVG>): Resource<PictureDrawable> {
    val svg = toTranscode.get()
    val picture = svg.renderToPicture()
    val drawable = PictureDrawable(picture)
    return SimpleResource<PictureDrawable>(drawable)
  }

  override fun getId(): String {
    return "SVGDrawableTranscoder.au.com.myarticles"
  }

}