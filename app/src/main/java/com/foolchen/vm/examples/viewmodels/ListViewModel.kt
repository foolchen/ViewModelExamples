package com.foolchen.vm.examples.viewmodels

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.foolchen.vm.examples.ListActivity
import com.foolchen.vm.examples.data.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.InputStreamReader


///////////////////////////////////////////////////////////////////////////
// ViewModel用于为UI准备数据。
// 并且在Activity进行了旋转后会自动重新关联，故其数据可以立即使用，而不必等待再次重新加载。
// 在屏幕方向发生了旋转后，Activity/Fragment中注册的Observer.onChanged()方法会立即回调
// 最后一次的数据
// 而ViewModel.onCleared()方法仅在Activity/Fragment被销毁时才会被调用 ，此时执行清理操作
///////////////////////////////////////////////////////////////////////////

/**
 * 与[ListActivity]对应的ViewModel
 * @author chenchong
 * 2018/4/18
 * 上午10:26
 */
class ListViewModel : ViewModel() {
  private val TAG = "LiveViewModel"
  @SuppressLint("StaticFieldLeak")
  private lateinit var appContext: Context
  private val articles: MutableLiveData<List<Article>> by lazy {
    MutableLiveData<List<Article>>()
  }
  private val error: MutableLiveData<Throwable> by lazy {
    MutableLiveData<Throwable>()
  }

  fun setAppContext(context: Context) {
    appContext = context.applicationContext
  }

  override fun onCleared() {
    Log.i(TAG, "ListViewModel is cleared.")
  }

  /**
   * 获取文章列表
   */
  fun getArticles(): LiveData<List<Article>> = articles

  /**
   * 尝试动态改变数据（异步）
   */
  fun postValue() {
    Observable
        .create<List<Article>> {
          val data = ArrayList<Article>()
          data.add(Article(999, "异步数据", 10, 1,
              listOf(
                  "https://img1.xcarimg.com/parts/10631/14173/20180418004925929342475741849.jpg"),
              System.currentTimeMillis()))
          it.onNext(data)
          it.onComplete()
        }.subscribeOn(Schedulers.io()).subscribe(
            {
              articles.postValue(it)
            },
            { error -> error.printStackTrace() })
  }

  /**
   * 尝试动态改变数据（同步）
   */
  fun setValue() {
    val data = ArrayList<Article>()
    data.add(Article(998, "同步数据", 10, 1,
        listOf("https://img1.xcarimg.com/news/26898/28188/20180418100027214098914334083.jpg"),
        System.currentTimeMillis()))
    articles.value = data
  }

  // 从assets文件中读取数据
  fun loadAsynchronous() {
    Observable
        .create<List<Article>> {
          val assets = appContext.assets
          val inputStream = assets.open("data.json")

          val data = Gson().fromJson<List<Article>>(InputStreamReader(inputStream),
              object : TypeToken<List<Article>>() {}.type)
          inputStream.close()

          data.forEach { it.time *= 1000 }

          Log.d(TAG, "异步加载数据完成")
          it.onNext(data)
          it.onComplete()
        }
        .subscribeOn(Schedulers.io())
        .subscribe(
            // 此处是在子线程进行了回调
            // 如果调用了setValue方法，则会报出一个IllegalStateException，但是并不会导致crash
            // 该方法在Activity被关闭后仍然会回调（如果没有手动关闭），但是并不会回调的Activity（不会造成内存泄露）
            { articles.postValue(it) },
            { error -> this@ListViewModel.error.postValue(error) }
        )
  }
}