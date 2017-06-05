package au.com.myarticles.news.common.uiframework.presentation.images

import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.IOException
import java.io.InputStream

class SVGDecoder: ResourceDecoder<InputStream, SVG> {

  override fun decode(source: InputStream, width: Int, height: Int): Resource<SVG> {
    try {
      val svg = SVG.getFromInputStream(source);
      return SimpleResource<SVG>(svg);
    } catch (ex: SVGParseException) {
      throw IOException("Cannot load SVG from stream", ex);
    }
  }

  override fun getId(): String? {
    return "SvgDecoder.au.com.myarticles";
  }

}

