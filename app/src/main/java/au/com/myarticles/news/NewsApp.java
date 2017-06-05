package au.com.myarticles.news;

import android.app.Application;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import au.com.myarticles.news.common.log.Logger;
import au.com.myarticles.news.di.ActivityComponent;
import au.com.myarticles.news.di.AppComponent;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import kotlin.Unit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class NewsApp extends Application {

  private static AppComponent appComponent;
  private static ActivityComponent currentActivityComponet;
  private static final String TAG = "AirwaysApp";

  @Inject
  Logger logger;

  private static BehaviorSubject<Unit> initFinishedBehaviour = BehaviorSubject.create();

  public static Observable<Unit> isInitialised() {
    return initFinishedBehaviour
        .asObservable()
        .first()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  @Override
  public void onCreate() {
    super.onCreate();

    initialise();
  }

  public void initialise() {
    createAppComponent();
    initialiseAppComponents();
  }

  private void initialiseAppComponents() {

    Observable.create(new Observable.OnSubscribe<Unit>() {
      @Override
      public void call(final Subscriber<? super Unit> subscriber) {
        try {
          initialiseRealmOrmConfig(NewsApp.this);
        } catch (Exception e) {
          logger.e(TAG, "error initialising app", e);
          subscriber.onError(e);
        }
      }
    }).subscribeOn(Schedulers.io()).subscribe(initFinishedBehaviour);
  }

  protected void createAppComponent() {
    appComponent = AppComponent.Initialiser.init(this);
    getAppComponent().inject(this);
  }

  @NotNull
  public static AppComponent getAppComponent() {
    return appComponent;
  }

  public static ActivityComponent getCurrentActivityComponent() {
    return currentActivityComponet;
  }

  public static void setCurrentActivityComponent(@NotNull ActivityComponent component) {
    currentActivityComponet = component;
  }

  private void initialiseRealmOrmConfig(Context context) {
    Realm.init(context);

    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
      .name(Realm.DEFAULT_REALM_NAME)
      .schemaVersion(0)
      .deleteRealmIfMigrationNeeded()
      .build();
    Realm.setDefaultConfiguration(realmConfiguration);
  }

}
