package com.foolchen.vm.examples.data

import com.google.gson.annotations.SerializedName

/**
 * @author chenchong
 * 2018/4/18
 * 上午10:24
 */
data class Article(
    @SerializedName("_id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("commentCount") var commentCount: Int,
    @SerializedName("type") var type: Int,
    @SerializedName("imageUrl") var images: List<String>,
    @SerializedName("time") var time: Long
)