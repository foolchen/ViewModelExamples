package com.foolchen.vm.examples

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    btn_list.setOnClickListener {
      startActivity(Intent(this@MainActivity, ListActivity::class.java))
    }
  }
}
