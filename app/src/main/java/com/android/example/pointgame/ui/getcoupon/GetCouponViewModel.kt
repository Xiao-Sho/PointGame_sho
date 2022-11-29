package com.android.example.pointgame.ui.getcoupon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GetCouponViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "獲得クーポン"
    }
    val text: LiveData<String> = _text
}