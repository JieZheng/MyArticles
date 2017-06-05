package au.com.myarticles.news.common.data

import au.com.myarticles.news.common.log.Logger
import io.realm.Realm
import io.realm.RealmObject
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

open class BaseCache <T : RealmObject>(val clazz: Class<T>) {
  protected val TAG = "BaseCache<" + clazz.name + ">"

  protected lateinit var logger: Logger
    @Inject set

  companion object {
    val REALM_ID = "realm_id"
  }

  open fun find(): Observable<T> {
    return wrapSearch({
      findExisting()
    })
  }

  open fun findAll(): Observable<List<T>> {
    return wrapSearchForAll({
      findAllExisting()
    })
  }

  open fun clearAndSet(data: T): Observable<T> {
    return wrapSaveOrReplace(data, {
      deleteAnyExisting()
    })
  }

  open fun clearAndSet(data: List<T>): Observable<List<T>> {
    return wrapSaveOrReplace(data, {
      deleteAnyExisting()
    })
  }

  open fun saveAll(list: List<T>): Observable<List<T>> {
    return wrapSaveOrReplace(list, null)
  }

  open fun save(obj: T): Observable<T> {
    return wrapSaveOrReplace(obj, null)
  }

  open fun saveOrReplaceWithProperty(data: T, propertyName: String, propertyValue: String): Observable<T> {
    return wrapSaveOrReplace(data, {
      deleteAnyExistingWithProperty(propertyName, propertyValue)
    })
  }

  open fun saveOrReplaceWithProperty(data: T, propertyName: String, propertyValue: Int): Observable<T> {
    return wrapSaveOrReplace(data, {
      deleteAnyExistingWithProperty(propertyName, propertyValue)
    })
  }

  open fun findForProperty(name: String, value: Int): Observable<T> {
    return wrapSearch({
      Realm.getDefaultInstance().let {
        it.where(clazz)
          .equalTo(name, value)
          .findFirst()
      }
    })
  }

  open fun findAllForProperty(name: String, value: Boolean): Observable<List<T>> {
    return wrapSearchForAll({
      Realm.getDefaultInstance().let {
        it.where(clazz)
          .equalTo(name, value)
          .findAll()
      }
    })
  }

  open fun findForProperty(name: String, value: String): Observable<T> {
    return wrapSearch({
      Realm.getDefaultInstance().let {
        it.where(clazz)
          .equalTo(name, value)
          .findFirst()
      }
    })
  }

  open fun findAllForProperty(name: String, value: String): Observable<List<T>> {
    return wrapSearchForAll({
      Realm.getDefaultInstance().let {
        it.where(clazz)
          .equalTo(name, value)
          .findAll()
      }
    })
  }

  protected open fun wrapSaveOrReplace(data: T, deleteFunc: (() -> Unit)?): Observable<T> {
    return Observable.create<T>() { subscriber ->
      try {
        deleteFunc?.invoke()
        Realm.getDefaultInstance().let {
          it.beginTransaction()
          it.copyToRealmOrUpdate(data)
          it.commitTransaction()
          it.close()
        }
        subscriber.onNext(data)
        subscriber.onCompleted()
      } catch (e: Exception) {
        logger.i(TAG, "Database error", e)
        subscriber.onError(e)
      }
    }.subscribeOn(Schedulers.io())
  }

  protected fun wrapSaveOrReplace(data: List<T>, deleteFunc: (() -> Unit)?): Observable<List<T>> {
    return Observable.create<List<T>>() { subscriber ->
      try {
        deleteFunc?.invoke()
        Realm.getDefaultInstance().let {
          it.beginTransaction()
          it.copyToRealmOrUpdate(data)
          it.commitTransaction()
          it.close()
        }
        subscriber.onNext(data)
        subscriber.onCompleted()
      } catch (e: Exception) {
        logger.i(TAG, "Database error", e)
        subscriber.onError(e)
      }
    }.subscribeOn(Schedulers.io())
  }

  protected open fun wrapSearch(searchFunc: (() -> T?)): Observable<T> {
    return Observable.create<T>() { subscriber ->
      try {
        val existing = searchFunc.invoke() ?: throw CacheMissError(clazz)
        subscriber.onNext(existing)
        subscriber.onCompleted()
      } catch (e: Exception) {
        logger.i(TAG, "Database error", e)
        subscriber.onError(e)
      }
    }.subscribeOn(Schedulers.io())
  }

  protected fun wrapSearchForAll(searchFunc: (() -> List<T>?)): Observable<List<T>> {
    return Observable.create<List<T>>() { subscriber ->
      try {
        val existing = searchFunc.invoke()
        if (existing == null || existing.size == 0) {
          subscriber.onError(CacheMissError(clazz, "FindAll"))
        } else {
          subscriber.onNext(existing)
          subscriber.onCompleted()
        }

      } catch (e: Exception) {
        logger.i(TAG, "Database error", e)
        subscriber.onError(e)
      }
    }.subscribeOn(Schedulers.io())
  }

  open fun deleteAnyExisting() {
    Realm.getDefaultInstance().let {
      it.beginTransaction()
      it.delete(clazz)
      it.commitTransaction()
      it.close()
    }
  }

  open fun deleteAnyExistingWithProperty(name: String, value: String) {
    Realm.getDefaultInstance().let {
      val results = it.where(clazz).equalTo(name, value).findAll()
      it.beginTransaction()
      results.deleteAllFromRealm()
      it.commitTransaction()
      it.close()
    }
  }

  protected open fun deleteAnyExistingWithProperty(name: String, value: Boolean) {
    Realm.getDefaultInstance().let {
      val results = it.where(clazz).equalTo(name, value).findAll()
      it.beginTransaction()
      results.deleteAllFromRealm()
      it.commitTransaction()
      it.close()
    }
  }

  protected open fun deleteAnyExistingWithProperty(name: String, value: Int) {
    Realm.getDefaultInstance().let {
      val results = it.where(clazz).equalTo(name, value).findAll()
      it.beginTransaction()
      results.deleteAllFromRealm()
      it.commitTransaction()
      it.close()
    }
  }

  protected open fun findExisting(): T? {
    return Realm.getDefaultInstance().where(clazz).findFirst()
  }

  protected open fun findAllExisting(): List<T> {
    return Realm.getDefaultInstance().where(clazz).findAll()
  }
}
