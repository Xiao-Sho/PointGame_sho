package com.android.example.pointgame.ui.home

import android.R
import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Point
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel


private const val START_X = 187
private const val START_Y = 104

class HomeContentViewModel : ViewModel() {
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home content Fragment"
//    }
//    val text: LiveData<String> = _text

    // サイコロマス（黄色マス）に到達したときのフラグ
    private var isDiceSquare = false

    val translations: List<Point> = listOf(
        Point(170 - START_X,525 - START_Y),   // 170, 525
        Point(543 - START_X, 525 - START_Y),  // 543, 525
        Point(888 - START_X, 525 - START_Y),  // 888, 525
        Point(888 - START_X, 974 - START_Y),  // 888, 974
        Point(543 - START_X, 974 - START_Y),  // 543, 974
        Point(170 - START_X, 974 - START_Y),  // 170, 974
        Point(170 - START_X, 1431 - START_Y), // 170, 1431
        Point(543 - START_X, 1431 - START_Y), // 543, 1431
        Point(888 - START_X, 1431 - START_Y), // 888, 1431
        Point(888 - START_X, 1857 - START_Y)  // 888, 1857
    )

    // 星を移動させる
    fun startMoveToPointAnim(fa: FragmentActivity, cl: ConstraintLayout, targetView: View, count: Int) {
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, translations[count].x.toFloat())
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, translations[count].y.toFloat())
        val target = ObjectAnimator.ofPropertyValuesHolder(targetView, translationX, translationY).apply {
            duration = 1000 // 1秒
            interpolator = DecelerateInterpolator() // 急速に開始し、その後減速しながら変化させる
        }

        // 次マスがサイコロマス（黄色マス）の場合
        if (count == 4 || count == translations.size - 1) {
            isDiceSquare = true
        }

        // セットアップしてスタート
        setupObjectAnimation(fa, cl, target)
        target.start()
    }

    // 星のアニメーション設定
    private fun setupObjectAnimation(fa: FragmentActivity, cl: ConstraintLayout, objectAnimation: ObjectAnimator) {
        objectAnimation.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(anim: Animator?) {
                Log.v("objectAnimation", "onAnimationStart")
            }
            override fun onAnimationEnd(anim: Animator?) {
                Log.v("objectAnimation", "onAnimationEnd")
                // objectAnimation.removeAllListeners()
                // サイコロマス（黄色マス）に到達した場合
                if (isDiceSquare) {
//                    // サイコロを表示、星を非表示、Moveボタンを非表示
//                    dice.visibility = View.VISIBLE
//                    targetView.visibility = View.INVISIBLE
//                    moveButton.visibility = View.INVISIBLE
//
//                    dice.playAnimation()

                    // フラグメントマネージャーの取得
                    val manager: FragmentManager = fa.supportFragmentManager
                    // フラグメントトランザクションの開始
                    val transaction: FragmentTransaction = manager.beginTransaction()
                    // HomeDiceFragmentに切替え
                    transaction.replace(cl.id, HomeDiceFragment());
                    // フラグメントトランザクションのコミット。コミットすることでFragmentの状態が反映される
                    transaction.commit()

                    isDiceSquare = false
                }
            }
            override fun onAnimationRepeat(anim: Animator?) {
                Log.v("objectAnimation", "onAnimationRepeat")
            }
            override fun onAnimationCancel(anim: Animator?) {
                Log.v("objectAnimation", "onAnimationCancel")
            }
        })
    }
}