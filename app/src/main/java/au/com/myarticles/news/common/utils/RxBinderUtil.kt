package au.com.myarticles.news.common.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

class RxBinderUtil(target: Any) {

  private val BINDER_TAG: String = this.javaClass.canonicalName
  private val targetTag = target.javaClass.canonicalName
  private val compositeSubscription = CompositeSubscription()
  private val registeredCallbacks = HashMap<Any, RegisteredCallbacksSet<out Any>>()
  private val handler = Handler(Looper.getMainLooper())

  fun clear() {
    compositeSubscription.clear()
    for ((sender, registeredCallbacksSet) in registeredCallbacks) {
      registeredCallbacksSet.clear()
    }
    registeredCallbacks.clear()
  }

  fun <U> bindProperty(
      observable: Observable<U>,
      setter: ((value: U) -> Unit)?,
      error: ((value: Throwable) -> Unit)? = null,
      complete: (() -> Unit)? = null
  ) {
    compositeSubscription.add(subscribeSetter(targetTag, observable, setter, error, complete))
  }

  fun <INSTANCE : Any, VALUE : Any> bindCallback(
      sender: INSTANCE,
      registerFunction: INSTANCE.(((VALUE) -> Unit)?) -> Unit,
      callback: (VALUE) -> Unit
  ) {
    @Suppress("UNCHECKED_CAST")
    var callbacksForSender = registeredCallbacks.get(sender) as RegisteredCallbacksSet<INSTANCE>?
    if (callbacksForSender == null) {
      callbacksForSender = RegisteredCallbacksSet(sender)
      registeredCallbacks.put(sender as Any, callbacksForSender)
    }
    callbacksForSender.registerCallbackWithValue<VALUE>(registerFunction, callback)
  }

  private fun <U> subscribeSetter(
      tag: String, observable: Observable<U>,
      setter: ((U) -> Unit)?,
      error: ((value: Throwable) -> Unit)? = null,
      complete: (() -> Unit)? = null
  ): Subscription {
    return observable.observeOn(AndroidSchedulers.mainThread())
        .subscribe(SetterSubscriber(tag, setter, error, complete))
  }

  private inner class SetterSubscriber<U>(
      val tag: String,
      val setter: ((value: U) -> Unit)?,
      val error: ((value: Throwable) -> Unit)?,
      val complete: (() -> Unit)?
  ) : Subscriber<U>() {

    override fun onCompleted() {
      Log.v(BINDER_TAG, tag + "." + "onCompleted")
      complete?.invoke()
    }

    override fun onError(e: Throwable) {
      val msg = tag + "." + "onError"
      Log.e(BINDER_TAG, msg, e)

      error?.invoke(e)
    }

    override fun onNext(u: U) {
      setter?.invoke(u)
    }

  }

  private inner class RegisteredCallbacksSet<T>(private val instance: T) {

    private val errorCallbacks = HashSet<T.(((Any) -> Unit)?) -> Unit>()
    private val callbacks = HashSet<T.((() -> Unit)?) -> Unit>()

    // wrap callback in handler to ensure it's always on the UI thread
    fun <VALUE : Any> registerCallbackWithValue(registerFunction: T.(((VALUE) -> Unit)?) -> Unit, callback: (VALUE) -> Unit) {
      instance.registerFunction { handler.post { callback.invoke(it) } }
      errorCallbacks.add(registerFunction)
    }

    fun registerCallback(registerFunction: T.((() -> Unit)?) -> Unit, callback: () -> Unit) {
      instance.registerFunction { handler.post { callback.invoke() } }
      callbacks.add(registerFunction)
    }


    fun clear() {
      for (callback in callbacks) {
        instance.callback(null)
      }

      for (errorCallback in errorCallbacks) {
        instance.errorCallback(null)
      }
    }

    override fun equals(other: Any?): Boolean {
      return if (other is RegisteredCallbacksSet<*>) {
        instance != null && instance.equals(other.instance)
      } else {
        false
      }
    }

  }

}
