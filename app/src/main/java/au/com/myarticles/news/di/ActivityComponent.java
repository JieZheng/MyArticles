package au.com.myarticles.news.di;

import android.app.Activity;
import com.squareup.otto.Bus;
import au.com.myarticles.news.NewsApp;
import au.com.myarticles.news.di.modules.ActivityModule;
import au.com.myarticles.news.di.scopes.ForActivity;
import au.com.myarticles.news.home.domain.NewsListViewModel;
import au.com.myarticles.news.home.presentation.HomeFragment;
import au.com.myarticles.news.home.presentation.HomeActivity;
import au.com.myarticles.news.home.presentation.SuggestedNewsActivity;
import au.com.myarticles.news.home.presentation.SuggestedNewsFragment;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
@ForActivity
public interface ActivityComponent {

  void inject(HomeActivity activity);

  void inject(HomeFragment fragment);

  void inject(SuggestedNewsActivity activity);

  void inject(SuggestedNewsFragment fragment);

  Bus getEventBus();

  NewsListViewModel getNewsListViewModel();

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
