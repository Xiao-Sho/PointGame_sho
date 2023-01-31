package com.android.example.pointgame.ui.getcoupon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GetCouponViewModel : ViewModel() {
    // タイトル
    private val _text = MutableLiveData<String>().apply {
        value = "獲得クーポン"
    }
    val text: LiveData<String> = _text

    // 獲得クーポン
    private val _coupons = MutableLiveData<MutableList<Coupon>>().apply {
        value = mutableListOf()
    }
    //可変式配列　保持する変数
    var coupons: MutableLiveData<MutableList<Coupon>> = _coupons
}
//☆
class Coupon(dateTime: String, name: String) {
    val dateTime: String = dateTime
    val name: String = name
    var isUsed: Boolean = false
}