package com.foolchen.vm.examples

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.foolchen.vm.examples.adapter.ListAdapter
import com.foolchen.vm.examples.data.Article
import com.foolchen.vm.examples.viewmodels.ListViewModel
import kotlinx.android.synthetic.main.activity_list.*

/**
 * @author chenchong
 * 2018/4/18
 * 上午10:23
 */
class ListActivity : AppCompatActivity() {
  private lateinit var vm: ListViewModel
  private val adapter = ListAdapter(null)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)

    vm = ViewModelProviders.of(this).get(ListViewModel::class.java)
    vm.setAppContext(this.applicationContext)
    vm.getArticles().observe(this, Observer<List<Article>> { data ->
      adapter.set(data)
    })
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    btn_reload_data.setOnClickListener {
      vm.loadAsynchronous()
    }
    btn_change_value_async.setOnClickListener {
      vm.postValue()
    }
    btn_change_value_sync.setOnClickListener {
      vm.setValue()
    }

    rv.layoutManager = LinearLayoutManager(this)
    rv.adapter = adapter
  }

}