package com.foolchen.vm.examples.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foolchen.vm.examples.R
import com.foolchen.vm.examples.data.Article
import com.foolchen.vm.examples.glide.GlideApp
import kotlinx.android.synthetic.main.item_list.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author chenchong
 * 2018/4/18
 * 上午11:45
 */
class ListAdapter(data: List<Article>?) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
  private val data = ArrayList<Article>()
  private val formatter = SimpleDateFormat("yyyy-MM-dd mm:ss", Locale.getDefault())

  init {
    if (data != null) {
      this.data.addAll(data)
    }
  }

  fun set(data: List<Article>?) {
    this.data.clear()
    if (data != null) {
      this.data.addAll(data)
    }

    notifyDataSetChanged()
  }


  override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListViewHolder {
    return ListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
  }

  override fun getItemCount(): Int = this.data.size

  override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
    holder.itemView.apply {
      val item = this@ListAdapter.data[position]
      if (item.images.isNotEmpty()) {
        GlideApp
            .with(iv)
            .load(item.images[0])
            .centerCrop()
            .into(iv)
      } else {
        GlideApp
            .with(iv)
            .load(R.mipmap.ic_launcher)
            .centerCrop()
            .into(iv)
      }
      tv_title.text = item.title
      tv_time.text = formatter.format(item.time)
    }
  }

  inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

