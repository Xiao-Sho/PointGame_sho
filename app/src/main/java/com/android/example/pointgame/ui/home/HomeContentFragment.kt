package com.android.example.pointgame.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.example.pointgame.R
import com.android.example.pointgame.databinding.FragmentHomeContentBinding
import com.android.example.pointgame.databinding.FragmentHomePagerBinding
import java.io.File


class HomeContentFragment(private val position: Int, private val pagerBinding: FragmentHomePagerBinding) : Fragment() {
    private var _binding: FragmentHomeContentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var sum = 0
    private var count = 0


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val homeContentViewModel =
            ViewModelProvider(this)[HomeContentViewModel::class.java]


        // バインディング
        _binding = FragmentHomeContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //sum,posの変数にファイルの数字を入れる
//        DataLoad()

       //textView表示
        binding.textView.text=sum.toString()

//        homeContentViewModel.startMoveToPointAnimtest(requireActivity(),
//            binding.fragmentHomeContentConstraintLayout, binding.fragmentHomeContentTarget, count, pagerBinding)

        // スワイプ位置によって背景画像を動的に変更する
        when (position) {
            0 -> {
                DataLoad()
                binding.fragmentHomeContentBackground.setImageResource(R.drawable.suica_stamp_rally_background)
                binding.fragmentHomeContentTarget.setImageResource(R.drawable.suica_stamp_rally_train)
                binding.fragmentHomeContentMidIcon.setImageResource(R.drawable.suica_stamp_rally_mid_icon)
                binding.fragmentHomeContentGoalIcon.setImageResource(R.drawable.suica_stamp_rally_goal_icon)
                //binding.fragmentHomeContentPondImage.visibility = View.VISIBLE
                homeContentViewModel.startMoveToPointAnimtest(requireActivity(),
                    binding.fragmentHomeContentConstraintLayout, binding.fragmentHomeContentTarget,
                    count, pagerBinding)

                pointgametest()
                DataSave()
            }
            else -> {
                DataLoad2()
                binding.fragmentHomeContentBackground.setImageResource(R.drawable.viewcard_stamp_rally_background)
                binding.fragmentHomeContentTarget.setImageResource(R.drawable.viewcard_stamp_rally_user)
                binding.fragmentHomeContentMidIcon.setImageResource(R.drawable.viewcard_stamp_rally_mid_icon)
                binding.fragmentHomeContentGoalIcon.setImageResource(R.drawable.viewcard_stamp_rally_goal_icon)
                //binding.fragmentHomeContentPondImage.visibility = View.GONE
                homeContentViewModel.startMoveToPointAnimtest(requireActivity(),
                    binding.fragmentHomeContentConstraintLayout, binding.fragmentHomeContentTarget,
                    count, pagerBinding)

                pointgametest()
                DataSave2()
            }
        }

//        //ログイン後の挙動
////        binding.fragmentHomeContentMoveButton.setOnClickListener{
//            val money = arrayOf(1000,1000,1000).random() //追加分
//            sum = sum + money
//            binding.textView.text=sum.toString()
//            var n = (money-money%500)/500 //追加分
//
//
//            while(n>0){
//                // ゴールに到達していない場合
//                if (count < homeContentViewModel.translations.size && count < 10) {
//                    count++
//                    homeContentViewModel.startMoveToPointAnim(requireActivity(),
//                        binding.fragmentHomeContentConstraintLayout, binding.fragmentHomeContentTarget,
//                        count, pagerBinding)
//                }
//                n = n-1
//            }
////            }


//　　　データセーブ




//        // マスの座標位置調査用
//        root.setOnTouchListener { _, event ->
//            when(event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    Log.v("ACTION_DOWN", "X:${event.x.toInt()}, Y:${event.y.toInt()}")
//                }
//                MotionEvent.ACTION_UP -> {
//                    Log.v("ACTION_UP", "X:${event.x.toInt()}, Y:${event.y.toInt()}")
//                }
//            }
//            true
//        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun DataSave(){

        requireContext().openFileOutput("moneysum", AppCompatActivity.MODE_PRIVATE).bufferedWriter().use{
            it.write(sum.toString())
        }
        requireContext().openFileOutput("position", AppCompatActivity.MODE_PRIVATE).bufferedWriter().use{
            it.write(count.toString())
        }
    }

    fun DataLoad(){

        var data=""
        val sumfile = File("/data/data/com.android.example.pointgame/files/moneysum")
        if(sumfile.exists()) {
            requireContext().openFileInput("moneysum").bufferedReader().forEachLine {
                data += it
            }
            sum = data.toInt()
        }
        else{
            sum = 0
        }
        data=""
        val posfile = File("/data/data/com.android.example.pointgame/files/position")
        if(posfile.exists()){
            requireContext().openFileInput("position").bufferedReader().forEachLine {
                data += it
            }
            count = data.toInt()
        }
        else{
            count=0
        }

    }

    fun DataSave2(){

        requireContext().openFileOutput("moneysum2", AppCompatActivity.MODE_PRIVATE).bufferedWriter().use{
            it.write(sum.toString())
        }
        requireContext().openFileOutput("position2", AppCompatActivity.MODE_PRIVATE).bufferedWriter().use{
            it.write(count.toString())
        }
    }

    fun DataLoad2(){

        var data=""
        val sumfile = File("/data/data/com.android.example.pointgame/files/moneysum2")
        if(sumfile.exists()) {
            requireContext().openFileInput("moneysum2").bufferedReader().forEachLine {
                data += it
            }
            sum = data.toInt()
        }
        else{
            sum = 0
        }
        data=""
        val posfile = File("/data/data/com.android.example.pointgame/files/position2")
        if(posfile.exists()){
            requireContext().openFileInput("position2").bufferedReader().forEachLine {
                data += it
            }
            count = data.toInt()
        }
        else{
            count=0
        }

    }


    fun pointgametest(){
        val homeContentViewModel =
        ViewModelProvider(this)[HomeContentViewModel::class.java]

        val money = arrayOf(1000,1000,1000).random() //追加分
        sum = sum + money
        binding.textView.text=sum.toString()
        var n = (money-money%500)/500 //追加分


        while(n>0){
            // ゴールに到達していない場合
            if (count < homeContentViewModel.translations.size && count < 10) {
                count++
                homeContentViewModel.startMoveToPointAnim(requireActivity(),
                    binding.fragmentHomeContentConstraintLayout, binding.fragmentHomeContentTarget,
                    count, pagerBinding)
            }
            n = n-1
        }
    }





}