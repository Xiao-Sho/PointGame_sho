package com.android.example.pointgame.ui.listcoupon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListCouponViewModel : ViewModel() {
    // タイトル
    private val _text = MutableLiveData<String>().apply {
        value = "クーポン一覧"
    }
    val text: LiveData<String> = _text
}