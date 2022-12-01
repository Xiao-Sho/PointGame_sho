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
        value = mutableListOf(
            Coupon("2022/09/20 00:00:00","ジュース引き換え券"),
            Coupon("2022/09/21 00:00:00","ジュース引き換え券"),
            Coupon("2022/09/22 00:00:00","ジュース引き換え券"))
    }
    val coupons: LiveData<MutableList<Coupon>> = _coupons
}

class Coupon(date: String, name: String) {
    val date: String = date
    val name: String = name
}