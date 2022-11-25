package com.android.example.pointgame.ui.getcoupon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GetCouponViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is get coupon Fragment"
    }
    val text: LiveData<String> = _text
}