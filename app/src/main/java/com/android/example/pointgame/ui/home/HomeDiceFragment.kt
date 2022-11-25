package com.android.example.pointgame.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.example.pointgame.R
import com.android.example.pointgame.databinding.FragmentHomeDiceBinding
import com.android.example.pointgame.databinding.FragmentHomePagerBinding


class HomeDiceFragment(private val pagerBinding: FragmentHomePagerBinding) : Fragment() {
    private var _binding: FragmentHomeDiceBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var handler: Handler

    var moveview: MoveView? = null

    //タップ位置Y軸の保持用
    var posY = 0f

    //ダイスの出目保持用
    var num = 1

    //初速、時間の保持用
    var v0 = 0f
    var t = 0

    //手を離した位置の保持用
    var detach = 0f

    //ダイスの状態を保持する変数
    var dice = state.Before

    var baseline = 0f

    //ダイスの画像保持用
    lateinit var img_dice: Array<Bitmap>

    //ダイスの状態を管理する列挙型クラス
    enum class state {
        Before, Touch, Toss, After
    }

    //データクラスでダイス関係の変数を構造体にして扱う方が良さそう（1個しか投げないから不要といえば不要...？）
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDiceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val view = inflater.inflate(R.layout.fragment_home_dice, container, false)
        //val layout = view.findViewById<ConstraintLayout>(R.id.fragment_home_dice_constraint_layout)

        val layout = binding.fragmentHomeDiceConstraintLayout

        //動的ビューをレイアウトに配置する
        moveview = MoveView(this.activity)
        layout.addView(moveview)
        layout.setWillNotDraw(false)

        //画像イメージを読み込む
        img_dice = arrayOf(
            BitmapFactory.decodeResource(resources, R.drawable.dice_10),
            BitmapFactory.decodeResource(resources, R.drawable.dice_20),
            BitmapFactory.decodeResource(resources, R.drawable.dice_30),
            BitmapFactory.decodeResource(resources, R.drawable.dice_40),
            BitmapFactory.decodeResource(resources, R.drawable.dice_50),
            BitmapFactory.decodeResource(resources, R.drawable.dice_60),
            BitmapFactory.decodeResource(resources, R.drawable.dice_11),
            BitmapFactory.decodeResource(resources, R.drawable.dice_21),
            BitmapFactory.decodeResource(resources, R.drawable.dice_31),
            BitmapFactory.decodeResource(resources, R.drawable.dice_41),
            BitmapFactory.decodeResource(resources, R.drawable.dice_51),
            BitmapFactory.decodeResource(resources, R.drawable.dice_61)
        )


        //動的ビューを実行
        animrun(moveview!!)

        //return view
        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ビューにリスナーを設定
        view.setOnTouchListener { _, event ->
            //画面を押下または移動したとき
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                //ダイスが静止/押下中の処理
                if (dice == state.Before || dice == state.Touch) {
                    //指の位置にダイスを追従させ、ダイスの状態はtouchに設定
                    posY = event.y
                    dice = state.Touch
                }
                //ダイスが停止後の処理
                else if (dice == state.After) {
                    // フラグメントマネージャーの取得
                    val manager: FragmentManager = requireActivity().supportFragmentManager
                    // 遷移前のフラグメントに戻る
                    manager.popBackStack()
                    // すぐろく画面に遷移した後に、スワイプを有効にする
                    pagerBinding.fragmentHomePager.isUserInputEnabled = true
                    pagerBinding.fragmentHomeFrameLayout.visibility = View.VISIBLE
                }
            }
            //画面から指を離したとき
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                //下に引っ張った状態且つダイスがタップ中の場合
                //※ダイスが空中や停止後ステータスの場合は動かさない為
                if (posY > baseline && dice == state.Touch) {
                    dice = state.Toss
                    //初速を設定
                    v0 = (posY - baseline) * 1.5f
                    //最大初速を制限
                    if (v0 > 1000) v0 = 1000f
                    //投げ上げの開始点(手を離した場所)を設定
                    detach = posY
                }
                //上にある状態で手を離したら状態を戻す
                else if (dice == state.Touch) {
                    dice = state.Before
                    posY = baseline
                    num = (0..5).random()
                }
            }

            true
        }

        view.post {
            baseline = view.height / 8 * 6f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        //サブスレッド終了
        handler.removeCallbacksAndMessages(null)
    }

    fun animrun(mv: MoveView) {
        handler = Handler(Looper.getMainLooper())

        //サブスレッドで実行
        handler.post(object : Runnable {
            //Cでいうmain関数
            override fun run() {

                //ダイスの状態に
                when (dice) {
                    //投げる前
                    state.Before -> {
                        //位置を固定する（この行は必要ないけど一応）
                        posY = baseline
                    }
                    //タップ中
                    state.Touch -> {
                        //番号をランダムで入れ替える
                        num = (0..11).random()
                    }
                    //空中
                    state.Toss -> {
                        //番号をランダムで入れ替える
                        num = (0..11).random()
                        //時間を進める
                        t += 2
                        //位置を移動する
                        posY = (detach - ((v0 * t - 0.5 * 9.8 * t * t)) / 5).toFloat()
                        //状態を確認する
                        state_check()
                    }
                    //投げた後
                    state.After -> {
                    }
                }
                //再描画
                mv.invalidate()
                handler.postDelayed(this, 0)
            }
        })
    }


    //ダイス状態を初期化
    private fun dice_reset() {
        dice = state.Before
        v0 = 0f
        t = 0
    }

    //ダイスが空中にある際の状態を確認
    private fun state_check() {
        //ベースラインより下まで落ちている場合
        if (posY >= baseline) {
            //初速100以下の場合は静止
            if (v0 <= 100) {
                posY = baseline
                dice = state.After

                //以下初期化処理
                //dice_reset()

                //サイコロの状態は0~5でランダム設定。ここで確定する
                num = (0..5).random()

                when (num) {
                    0, 3 -> {
                        binding.fragmentHomeDiceGetCouponMessageImageView.setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.get_coupon_message_1, null)
                        )
                    }
                    1, 4 -> {
                        binding.fragmentHomeDiceGetCouponMessageImageView.setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.get_coupon_message_2, null)
                        )
                    }
                    2, 5 -> {
                        binding.fragmentHomeDiceGetCouponMessageImageView.setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.get_coupon_message_3, null)
                        )
                    }
                    else -> {
                        binding.fragmentHomeDiceGetCouponMessageImageView.setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.get_coupon_message_1, null)
                        )
                    }
                }

                //クーポン獲得メッセージを表示する
                binding.fragmentHomeDiceGetCouponMessageImageView.visibility = View.VISIBLE

                //トースト表示
                Toast.makeText(activity, "画面をタッチするとスタンプラリー画面に戻ります", Toast.LENGTH_LONG).show()
            } else {//初速が100越えの場合は初速を半分にして反発
                //反発したポジションを基準線に設定し、経過時間を0にする
                posY = baseline
                detach = baseline
                t = 0
                v0 /= 2
            }
        }
    }


    //動的ビューのクラス
    inner class MoveView : View {
        constructor(context: Context?) : super(context)
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
        )

        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)

            val paint = Paint()

            //キャンバスの状態を保存
            canvas!!.save()

            //画像サイズを小さくするため一時的にスケールを20%に設定
            canvas.scale(0.2f, 0.2f)

            //ダイスのイメージを描画
            //width*2.5にしている理由↓
            //20%になっているので全体の幅を5倍にすると通常幅、それの半分の位置にしたいので2.5倍
            canvas.drawBitmap(
                img_dice[num],
                width * 2.5f - (img_dice[num].width / 2f),
                posY * 5 - img_dice[num].height / 2,
                paint
            )

            //スケールを100%に戻す(リストアする)
            canvas.restore()

            //以下デバッグ
            //       ペイントする色の設定
            //paint.color = Color.argb(255, 0, 0, 255)
            // ペイントストロークの太さを設定
            //paint.strokeWidth = 2f

            // Styleのストロークを設定する
            //paint.style = Paint.Style.STROKE

            //canvas.drawRect(width / 2f, 0f, width / 2f, height*1f, paint)
            //canvas.drawRect(0f, height/2f, width*1f, height/2f, paint)

            //paint.color = Color.argb(255, 255, 0, 0)
            //canvas.drawRect(0f, posY, width*1f, posY, paint)
        }
    }
}