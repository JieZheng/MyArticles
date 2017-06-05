package au.com.myarticles.news.common.uiframework.presentation

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import au.com.myarticles.news.R
import au.com.myarticles.news.common.utils.getBoolean
import au.com.myarticles.news.di.ActivityComponent
import com.squareup.otto.Bus
import javax.inject.Inject
import kotlin.properties.Delegates

public abstract class BaseActivity : AppCompatActivity() {

  public lateinit var bus: Bus
    @Inject set

  protected lateinit var context: Context
    @Inject set

  protected var component: ActivityComponent by Delegates.notNull()

  abstract protected fun inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    inject()

    if (getBoolean(R.bool.portrait_only)) {
      requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
  }

  override fun onResume() {
    super.onResume()
  }

  override fun onPause() {
    super.onPause()
  }

  fun getActivityComponent(): ActivityComponent? {
    return component
  }
}