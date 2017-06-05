package au.com.myarticles.news.common.uiframework.presentation.views

import android.content.Context
import android.util.AttributeSet

class NormalImageView : DownloadableImageView {

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

  fun loadImage(imageUrl: String, placeHolderImage: Int? = null, onSuccess: (() -> Unit)? = null, errorImage: Int? = null, onError: (() -> Unit)? = null,
                forceRefresh: Boolean = false) {
    imageUrl.let {
      setCustomImagePath(it,
        onSuccess = onSuccess,
        placeHolderImage = placeHolderImage,
        errorImage = errorImage,
        onError = onError,
        forceRefresh = forceRefresh)
    }
  }

  fun loadImageNoDefaultImages(imageUrl: String, onSuccess: (() -> Unit)? = null, onError: (() -> Unit)? = null) {
    imageUrl.let {
      setCustomImagePath(it, onSuccess, null, null, onError)
    }
  }
}

