package au.com.myarticles.news.di;

import android.app.Activity;
import com.squareup.otto.Bus;
import au.com.myarticles.news.NewsApp;
import au.com.myarticles.news.di.modules.ActivityModule;
import au.com.myarticles.news.di.scopes.ForActivity;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
@ForActivity
public interface ActivityComponent {

  Bus getEventBus();

  final class Initialiser {
    public static ActivityComponent init(Activity activity) {
      ActivityComponent component = DaggerActivityComponent.builder()
        .activityModule(new ActivityModule(activity))
        .appComponent(NewsApp.getAppComponent())
        .build();
      NewsApp.setCurrentActivityComponent(component);

      return component;
    }
  }
}
