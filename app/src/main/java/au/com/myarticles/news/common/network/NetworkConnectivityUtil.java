package au.com.myarticles.news.common.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NetworkConnectivityUtil {

  @Inject
  ConnectivityManager connectivityManager;

  @Inject
  public NetworkConnectivityUtil() {}

  public boolean isConnected() {
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
