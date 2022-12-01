package com.android.example.pointgame.ui.getcoupon

import android.view.Gravity
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.pointgame.R
import com.android.example.pointgame.util.dp
import com.android.example.pointgame.util.sp
import com.google.android.material.button.MaterialButton

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
    var coupons: MutableLiveData<MutableList<Coupon>> = _coupons
}

class Coupon(dateTime: String, name: String) {
    val dateTime: String = dateTime
    val name: String = name
    val isUsed: Boolean = false
}