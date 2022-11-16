package com.android.example.pointgame.ui.home

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Point
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import com.android.example.pointgame.databinding.FragmentHomePagerBinding


private const val START_X = 187
private const val START_Y = 104

class HomeContentViewModel : ViewModel() {
    // サイコロマス（黄色マス）に到達したときのフラグ
    private var isDiceSquare = false

    // 各マスの座標
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
    fun startMoveToPointAnim(fa: FragmentActivity, cl: ConstraintLayout, targetView: View, count: Int, pagerBinding: FragmentHomePagerBinding) {
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
        setupObjectAnimation(fa, cl, target, pagerBinding)
        target.start()
    }

    // 星のアニメーション設定
    private fun setupObjectAnimation(fa: FragmentActivity, cl: ConstraintLayout, objectAnimation: ObjectAnimator, pagerBinding: FragmentHomePagerBinding) {
        objectAnimation.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(anim: Animator) {
                Log.v("objectAnimation", "onAnimationStart")
            }
            override fun onAnimationEnd(anim: Animator) {
                Log.v("objectAnimation", "onAnimationEnd")
                // objectAnimation.removeAllListeners()
                // サイコロマス（黄色マス）に到達した場合
                if (isDiceSquare) {
                    // フラグメントマネージャーの取得
                    val manager: FragmentManager = fa.supportFragmentManager
                    // フラグメントトランザクションの開始
                    val transaction: FragmentTransaction = manager.beginTransaction()
                    // HomeDiceFragmentに遷移
                    transaction.add(cl.id, HomeDiceFragment(pagerBinding))
                    // トランザクションを記録する
                    transaction.addToBackStack(null)

                    // サイコロ画面に遷移する前に、スワイプを無効にする
                    pagerBinding.fragmentHomeFrameLayout.visibility = View.INVISIBLE
                    pagerBinding.fragmentHomePager.isUserInputEnabled = false

                    // フラグメントトランザクションのコミット。コミットすることでFragmentの状態が反映される
                    transaction.commit()

                    // フラグ更新
                    isDiceSquare = false
                }
            }
            override fun onAnimationRepeat(anim: Animator) {
                Log.v("objectAnimation", "onAnimationRepeat")
            }
            override fun onAnimationCancel(anim: Animator) {
                Log.v("objectAnimation", "onAnimationCancel")
            }
        })
    }
}