package au.com.myarticles.news.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.util.Log;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class SystemServicesModule {

  private static final String TAG = SystemServicesModule.class.getCanonicalName();
  private final Application app;

  public SystemServicesModule(Application application) {
    this.app = application;
  }

  @Provides
  @Singleton
  public Context provideApplicationContext() {
    return app;
  }

  @Provides
  @Singleton
  public Resources provideResources(Context context) {
    return context.getResources();
  }

  @Provides
  @Singleton
  public ConnectivityManager provideConnectivityManager() {
    return (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Provides
  @Singleton
  public PackageInfo providePackageInfo(Context context) {
    try {
      return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(TAG, "News package not found. This won't happen.");
      return null;
    }
  }
}
