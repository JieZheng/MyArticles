package au.com.myarticles.news.home.presentation

import android.os.Bundle

import au.com.myarticles.news.R
import au.com.myarticles.news.common.uiframework.presentation.BaseActivity
import au.com.myarticles.news.di.ActivityComponent

class SuggestedNewsActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_suggested_news)
  }

  override fun onResume() {
    super.onResume()

    switchToHomeFragment()
  }

  override fun inject() {
    component = lastCustomNonConfigurationInstance as? ActivityComponent ?: ActivityComponent.Initialiser.init(this)
    component.inject(this)
  }

  fun switchToHomeFragment() {
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.frameLayout_suggested_news, SuggestedNewsFragment.create(), SuggestedNewsFragment.Fragment_Suggested_News)
      .commit()
  }
}
