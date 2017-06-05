package au.com.myarticles.news.common.uiframework.support

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import au.com.myarticles.news.R
import au.com.myarticles.news.common.log.Logger
import au.com.myarticles.news.common.uiframework.Component
import au.com.myarticles.news.common.uiframework.presentation.views.ErrorMessageUtil
import au.com.myarticles.news.common.utils.RxBinderUtil
import au.com.myarticles.news.common.utils.fixTopScrollingAfterInsertingElement
import com.squareup.otto.Bus
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import rx.Observable
import rx.Subscription
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates

abstract class GenericComponentFragment<T : Any>() : Fragment() {

  protected val binder = RxBinderUtil(this)

  protected var bus: Bus by Delegates.notNull()
    @Inject set

  protected var logger: Logger by Delegates.notNull()
    @Inject set

  protected var errorMessageUtil: ErrorMessageUtil by Delegates.notNull()
    @Inject set

  protected lateinit var currentStartingObject: T

  private var trackedEvents = ArrayList<Any>()

  protected abstract fun inject()

  companion object {
    private const val DEBOUNCE_IN_MILLISECONDS = 50L

    @JvmStatic fun generateBundle(): Bundle {
      val args: Bundle = Bundle()
      return args
    }
  }

  protected val TAG = javaClass.simpleName

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    inject()
    setRetained()

    setupListView()
  }

  protected open fun setRetained() {
    retainInstance = true
  }

  protected open fun setupViewModelStartingObjectUpdates() {
    binder.bindProperty(
      getStartingObject(),
      { startViewModel(it) },
      { errorMessageUtil.showSnackbar(view!!, R.string.general_error) })
  }

  protected open fun setupListView() {
    val recycler = view!!.findViewById(R.id.recycler) as RecyclerView
    recycler.adapter = ComponentListAdapter(bus, getAnimatedInitially())
    recycler.layoutManager = LinearLayoutManager(context)
    recycler.itemAnimator = FadeInAnimator()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.layout_display_element_list, container, false)
  }

  protected open fun getAnimatedInitially(): Boolean {
    return false
  }

  override fun onResume() {
    super.onResume()
    bus.register(this)
    trackPageView()

    setupViewModelStartingObjectUpdates()

    setupTimer()

    binder.bindProperty(
      getErrors().distinctUntilChanged(),
      {
        if (it != null) {
          errorMessageUtil.showSnackbar(view!!, R.string.general_error)
        }
      }
    )

    binder.bindProperty(
      getDisplayElements(),
      {
        setDisplayElements(it)
      }
    )

    setupSwipeToRefresh()

    binder.bindProperty(
      isRefreshing(),
      {
        val swipeToRefresh = getSwipeToRefreshLayout()
        swipeToRefresh?.isRefreshing = it
      }
    )
  }

  protected open fun setupSwipeToRefresh() {
    binder.bindProperty(
      canRefresh(),
      { canRefresh ->
        val swipeToRefresh = getSwipeToRefreshLayout()

        if (canRefresh) {
          swipeToRefresh?.setCanRefresh(true)
          swipeToRefresh?.isRefreshing = true
          swipeToRefresh?.setOnRefreshListener {
            // keep subscription even if we navigate away
            refresh().subscribe(
              {
                logger.d(TAG, "finished swipe to refresh")
              },
              {
                logger.w(TAG, "error swipe to refresh", it)
              })
          }
        } else {
          swipeToRefresh?.setCanRefresh(false)
        }
      }
    )
  }

  protected open fun setDisplayElements(displayElements: List<Component>) {
    val recycler = view!!.findViewById(R.id.recycler) as RecyclerView

    (recycler.adapter as ComponentListAdapter).setAndAnimate(displayElements)
    (recycler.layoutManager as LinearLayoutManager).fixTopScrollingAfterInsertingElement()
  }

  protected open fun getSwipeToRefreshLayout(): MySwipeRefreshLayout? {
    return view?.rootView?.findViewById(R.id.swipe_to_refresh) as MySwipeRefreshLayout?
  }

  override fun onPause() {
    super.onPause()
    binder.clear()

    bus.unregister(this)

    tearDownSwipeToRefresh()

    teardownTimer()
  }

  override fun onDestroy() {
    super.onDestroy()
    getViewModels().map { it.unsubscribe() }
  }

  protected open fun tearDownSwipeToRefresh() {
    val swipeToRefresh = getSwipeToRefreshLayout()
    swipeToRefresh?.setOnRefreshListener(null)
  }

  protected abstract fun getViewModels(): List<ComponentProducer<T>>
  protected abstract fun getStartingObject(): Observable<T>


  protected open fun isRefreshing(): Observable<Boolean> {
    val viewModelsRefreshingState = getViewModels().map { model ->
      getStartingObject()
        .switchMap { startingObject ->
          model
            .isRefreshing(startingObject)
            .map {
              Pair(model.javaClass.simpleName, it)
            }
            .doOnNext {
              val state = it.second
              logger.d(TAG,
                "model refreshing state changed model=${model.javaClass.simpleName}, state=$state")
            }
        }
    }

    return Observable.combineLatest(
      viewModelsRefreshingState,
      {
        @Suppress("UNCHECKED_CAST")
        val viewModelsStillRefreshing = it
          .map { it as Pair<String, Boolean> }
          .filter { it.second }

        if (viewModelsStillRefreshing.isNotEmpty()) {
          logger.d(TAG, "models still refreshing=${viewModelsStillRefreshing.map { it.first }.joinToString(", ")}")
          true
        } else {
          logger.d(TAG, "models finished refreshing")
          false
        }
      }
    )

  }

  protected open fun refresh(): Observable<Unit> {
    return getStartingObject()
      .take(1)
      .flatMap { startingObject ->
        Observable.merge(
          getViewModels()
            .map {
              it.prepareRefresh(startingObject)
              it
            }
            .map { viewModel ->
              viewModel
                .refresh(startingObject)
                .doOnCompleted { logger.d(TAG, "completed refresh for ${viewModel.javaClass.simpleName}") }
            }
        )
      }
  }

  /**
   * Whenever canRefresh returns false the SwipeToRefresh layout will hide the circular animation.
   *
   * If this update dynamically then you could see bugs where the circular animation disappears. Best
   * way to fix this issue is to only return a single event when the fragment loads.
   */
  abstract fun canRefresh(): Observable<Boolean>

  protected open fun startViewModel(startingObject: T) {
    this.currentStartingObject = startingObject
    getViewModels().forEach {
      it.go(startingObject)
    }
  }

  protected open fun getErrors(): Observable<Throwable?> {
    return Observable.merge(
      getViewModels().map { it.getErrors() }
    )
  }

  @Suppress("UNCHECKED_CAST")
  protected open fun getDisplayElements(): Observable<List<Component>> {
    var isFirst = true
    return Observable.combineLatest(
      getViewModels()
        .map { model ->
          model.getDisplayElements()
            .doOnNext {
              logger.d(TAG, "model ${model.javaClass.simpleName} displaying components")
            }
        },
      {
        logger.d(TAG, "start displaying components")
        it.flatMap {
          it as List<Component>
        }
      }
    ).debounce {
      if (isFirst) {
        isFirst = false
        Observable.empty<Long>()
      } else {
        Observable.timer(DEBOUNCE_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
      }
    }.doOnError {
      logger.e(TAG, "$it", it)
    }
  }

  protected var refreshLastUpdated: Long = 0

  private fun teardownTimer() {
    logger.d(TAG, "Ended Refresh timer")
    timerSubscription?.unsubscribe()
  }

  private var timerSubscription: Subscription? = null

  private fun setupTimer() {
    if (getTimerRefreshListener() != null && getTimerRefreshFrequency() > 0) {
      logger.d(TAG, "Starting Refresh timer")
      timerSubscription = Observable.interval(0, getTimerRefreshFrequency(), TimeUnit.MILLISECONDS)
        .subscribe({
          logger.d(TAG, "Refreshing...$it")
          getTimerRefreshListener()?.invoke()
          refreshLastUpdated = System.currentTimeMillis()
        }, {
          logger.d(TAG, "Error during timer", it)
        })
    }
  }

  open protected fun getTimerRefreshListener(): (() -> Unit)? {
    return null
  }

  open protected fun getTimerRefreshFrequency(): Long {
    return 0
  }

  open protected fun getAnalyticsPageName(): String? {
    return null
  }

  private fun trackPageView() {
    val title = getAnalyticsPageName()
  }

  protected fun checkEventTrackingStatus(event: Any): Boolean {
    if (trackedEvents.contains(event)) {
      return true
    }
    trackedEvents.add(event)
    return false
  }

}