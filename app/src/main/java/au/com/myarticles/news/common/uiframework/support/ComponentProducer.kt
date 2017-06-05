package au.com.myarticles.news.common.uiframework.support

import android.content.Context
import au.com.myarticles.news.common.log.Logger
import au.com.myarticles.news.common.uiframework.Component
import rx.Observable
import rx.exceptions.CompositeException
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

abstract class ComponentProducer<T : Any>() {

  protected val TAG_NAME = this.javaClass.simpleName

  lateinit var context: Context
    @Inject set

  lateinit var logger: Logger
    @Inject set

  protected var behaviour: BehaviorSubject<List<Component>> = BehaviorSubject.create()

  protected var errorBehaviour: PublishSubject<Throwable?> = PublishSubject.create()

  protected val compositeSubscription = CompositeSubscription()

  open fun go(startingObject: T) {
    compositeSubscription.clear()

    compositeSubscription.add(
      createDisplayElements(startingObject).subscribe(
        { behaviour.onNext(it) },
        {
          logger.w(TAG_NAME, "error creating elements", it)
          emitError(it)
        }
      )
    )
  }

  open fun unsubscribe() {
    compositeSubscription.clear();
  }

  abstract fun createDisplayElements(startingObject: T): Observable<List<Component>>

  abstract fun refresh(startingObject: T): Observable<Unit>

  open fun prepareRefresh(startingObject: T) {
  }

  abstract fun isRefreshing(startingObject: T): Observable<Boolean>

  fun getDisplayElements(): Observable<List<Component>> {
    return behaviour
  }

  open fun getErrors(): Observable<Throwable?> {
    return errorBehaviour
  }

  protected open fun emitError(e: Throwable) {
    if (e is CompositeException) {
      e.exceptions.forEach { errorBehaviour.onNext(it) }
    } else {
      errorBehaviour.onNext(e)
    }
  }

  protected open fun getString(stringId: Int): String {
    return context.resources.getString(stringId)
  }

}