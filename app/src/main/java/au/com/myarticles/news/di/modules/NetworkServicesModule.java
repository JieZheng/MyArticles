package au.com.myarticles.news.di.modules;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import au.com.myarticles.news.common.network.OkHttpStack;
import dagger.Module;
import dagger.Provides;

@Module
public class NetworkServicesModule {

  @Provides
  @Singleton
  public RequestQueue provideRequestQueue(Context applicationContext, OkHttpClient okHttpClient) {
    return Volley.newRequestQueue(applicationContext, new OkHttpStack(okHttpClient));
  }

  @Provides
  @Singleton
  public OkHttpClient provideOkHttpClient() {
    OkHttpClient client = new OkHttpClient();
    return client;
  }

}
