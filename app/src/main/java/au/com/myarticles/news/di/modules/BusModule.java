package au.com.myarticles.news.di.modules;

import com.squareup.otto.Bus;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class BusModule {

  @Provides
  @Singleton
  public Bus provideBus() {
    return new Bus();
  }

}
