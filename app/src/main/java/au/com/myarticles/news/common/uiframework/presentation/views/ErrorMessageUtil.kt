package au.com.myarticles.news.common.uiframework.presentation.views

import android.support.design.widget.Snackbar
import android.view.View
import au.com.myarticles.news.R
import javax.inject.Inject

class ErrorMessageUtil @Inject constructor() {

  public fun showSnackbar(view: View, msg: String) {
    Snackbar.make(view, msg, view.getResources().getInteger(R.integer.snackbar_long)).show()
  }

  public fun showSnackbar(view: View, msg: Int) {
    Snackbar.make(view, msg, view.getResources().getInteger(R.integer.snackbar_long)).show()
  }

}
