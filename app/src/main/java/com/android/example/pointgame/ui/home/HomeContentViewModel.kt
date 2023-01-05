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


private const val START_X = 199
private const val START_Y = 753

class HomeContentViewModel : ViewModel() {
    // サイコロマス（黄色マス）に到達したときのフラグ
    private var isDiceSquare = false

    // 各マスの座標
    val translations: List<Point> = listOf(
        Point(466 - START_X,720 - START_Y),
        Point(747 - START_X, 780 - START_Y),
        Point(969 - START_X, 910 - START_Y),
        Point(840 - START_X, 1200 - START_Y),
        Point(608 - START_X, 1300 - START_Y),
        Point(367 - START_X, 1350 - START_Y),
        Point(176 - START_X, 1500 - START_Y),
        Point(376 - START_X, 1740 - START_Y),
        Point(631 - START_X, 1750 - START_Y),
        Point(889 - START_X, 1800 - START_Y)
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