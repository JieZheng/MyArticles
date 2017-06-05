package au.com.myarticles.news.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import au.com.myarticles.news.NewsApp;
import au.com.myarticles.news.common.config.EnvironmentConfig;
import au.com.myarticles.news.common.log.Logger;
import au.com.myarticles.news.common.network.SerializerUtil;
import au.com.myarticles.news.common.network.services.NetworkService;
import au.com.myarticles.news.common.network.services.ServiceManager;
import au.com.myarticles.news.di.modules.BusModule;
import au.com.myarticles.news.di.modules.NetworkServicesModule;
import au.com.myarticles.news.di.modules.SystemServicesModule;
import au.com.myarticles.news.home.data.NewsDataLayer;
import au.com.myarticles.news.home.network.NewsService;
import dagger.Component;

@Singleton
@Component(modules = {SystemServicesModule.class, NetworkServicesModule.class, BusModule.class})
public interface AppComponent {

  void inject(NewsApp app);

  EnvironmentConfig getEnvironmentConfig();

  Logger getLogger();

  Bus getEventBus();

  SerializerUtil getSerializerUtil();

  NetworkService getNetworkService();

  Context getApplicationContext();

  Resources getResources();

  ServiceManager getServiceManager();

  NewsService getNewsService();

  NewsDataLayer getNewsDataLayer();

  final class Initialiser {
    public static AppComponent init(Application app) {
      return DaggerAppComponent.builder()
          .systemServicesModule(new SystemServicesModule(app))
          .build();
    }
  }
}
