package au.com.myarticles.news.di.modules;

import android.app.Activity;

import au.com.myarticles.news.di.scopes.ForActivity;
import dagger.Module;
import dagger.Provides;

@Module
@ForActivity
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides
  @ForActivity
  public Activity provideActivity() {
    return activity;
  }
}
