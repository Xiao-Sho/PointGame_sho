package com.android.example.pointgame.ui.getcoupon

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.example.pointgame.FileRW
import com.android.example.pointgame.R
import com.android.example.pointgame.databinding.FragmentGetCouponBinding
import com.android.example.pointgame.util.dp
import com.android.example.pointgame.util.sp
import com.google.android.material.button.MaterialButton
import java.util.*


class GetCouponFragment : Fragment() {
    private var _binding: FragmentGetCouponBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //    private var coupons = "null"
    // 共有ビューモデル
    private val sharedGetCouponViewModel: GetCouponViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // バインディング
        _binding = FragmentGetCouponBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Save
        CouponLoad()
//        CouponSave()
        //load
        //CouponLoad()

        // タイトル
        val textView: TextView = binding.fragmentGetCouponTitle
        sharedGetCouponViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // テーブル取得
        val tl: TableLayout = binding.fragmentGetCouponTableLayout

        // クーポンなし文言
        val noCouponText: TextView = binding.fragmentGetCouponNoCoupon

        sharedGetCouponViewModel.coupons.observe(viewLifecycleOwner) {
            // 獲得クーポンが何もない場合はテーブル自体を非表示、クーポンなし文言を表示
            // 獲得クーポンがある場合は、行追加を繰り返して表を作成

            if (it.isEmpty()) {
                tl.visibility = View.GONE
                noCouponText.visibility = View.VISIBLE
            } else {
                for (coupon in it) {
                    addRow(tl, coupon)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //ここでクーポン追加
    private fun addRow(tl: TableLayout, coupon: Coupon) {
        // 行用意
        val tr: TableRow = TableRow(requireContext())
        val dateTv: TextView = TextView(requireContext())
        val ll: LinearLayout = LinearLayout(requireContext())
        val couponTv: TextView = TextView(requireContext())
        val couponButton: Button = MaterialButton(requireContext())

        // TableRow属性
        tr.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        tr.background = ResourcesCompat.getDrawable(resources, R.drawable.border, null)

        // TextView(date)属性
        dateTv.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        ).apply {
            weight = 1.25f
        }
        dateTv.background = ResourcesCompat.getDrawable(resources, R.drawable.border, null)
        dateTv.setPadding(4.dp, dateTv.paddingTop, dateTv.paddingRight, dateTv.paddingBottom)
        dateTv.text = coupon.dateTime

        // LinearLayout属性
        ll.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        ).apply {
            weight = 1f
        }
        ll.setPadding(4.dp, ll.paddingTop, ll.paddingRight, ll.paddingBottom)
        ll.orientation = LinearLayout.VERTICAL

        // TextView(coupon)属性
        couponTv.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            25.dp
        )
        couponTv.text = coupon.name



        // Button属性
        couponButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            30.dp
        ).apply {
            gravity = Gravity.CENTER
            setMargins(
                couponButton.marginLeft,
                couponButton.marginTop,
                couponButton.marginRight,
                4.dp
            )
        }
        couponButton.width = 125.dp
        couponButton.background = ResourcesCompat.getDrawable(resources, R.drawable.border, null)
        //couponButton.gravity = Gravity.CENTER
        couponButton.setPadding(couponButton.paddingLeft, 0.dp, couponButton.paddingRight, 0.dp)
        couponButton.textSize = 4.5f.sp
        updateCouponButton(coupon.isUsed, couponButton)
        couponButton.setOnClickListener {
            activity?.let {
                val builder = AlertDialog.Builder(it)
                // Get the layout inflater
                val inflater = requireActivity().layoutInflater

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.qr_code_dialog, null))
                    // Add action buttons
                    .setPositiveButton("使用済にする",
                        DialogInterface.OnClickListener { _, _ ->
                            sharedGetCouponViewModel.coupons.observe(viewLifecycleOwner) {
                                coupon.isUsed = true
                                updateCouponButton(coupon.isUsed, couponButton)
                                CouponSave()
                            }
                        })
                    .setNegativeButton("キャンセル",
                        DialogInterface.OnClickListener { _, _ ->
                            // sign in the user ...
                        })
                builder.show()
            } ?: throw IllegalStateException("Activity cannot be null")

        }



        // 行追加
        ll.addView(couponTv)
        ll.addView(couponButton)
        tr.addView(dateTv)
        tr.addView(ll)
        tl.addView(tr)


    }



    private fun updateCouponButton(bool: Boolean, button: Button) {
        if (bool) {
            button.text = "使用済"
            button.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.red)
            button.isEnabled = false
        } else {
            button.text = "クーポン使用"
            button.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.purple_500)
        }
    }


    //書き出し
    fun CouponSave() {
        val sharedGetCouponViewModel =
            ViewModelProvider(this)[GetCouponViewModel::class.java]

        var data: String = ""
        for (it in sharedGetCouponViewModel.coupons.value!!) {
            data += it.dateTime + "," +it.name+ "," + it.isUsed + "\n"
        }
        FileRW().FileWrite(context, "coupon", data)

    }

//    fun CouponLoad() {
//
//        try {
//            val file = File("/data/data/com.android.example.pointgame/files/coupon")
//            var data=FileRW().FileRead(context,"coupon")
//            var sc = Scanner(file)
////            var sc = Scanner(File(data))
//
//            while (sc.hasNextLine()) {
//                val line = sc.nextLine()
//                println(line)
//            }
//                context?.openFileInput("coupon")?.bufferedReader()?.forEachLine { line ->
//                var a = data.split(",")
//                sharedGetCouponViewModel.coupons.observe(viewLifecycleOwner) { it ->
//                    it.add(Coupon(a[0], a[1],a[2].toBoolean()))
//                }
//            }
////            while (sc.hasNextLine()) {
////                var str = sc.next()
////                val line = sc.nextLine()
////
////                var a = line.split(",")
////                sharedGetCouponViewModel.coupons.observe(viewLifecycleOwner) { it ->
////                    it.add(Coupon(a[0], a[1], a[2].toBoolean()))
////                }
////        }
//        }
//        catch (_: Exception) {
//        }
//    }
fun CouponLoad() {

    try {
        var data = FileRW().FileRead(context, "coupon")

        val sc = Scanner(data)
        while (sc.hasNextLine()) {
            var str = sc.next()
            val line = sc.nextLine()

            var a = line.split(",")
            sharedGetCouponViewModel.coupons.observe(viewLifecycleOwner) { it ->
                it.add(Coupon(a[0], a[1], a[2].toBoolean()))
            }

        }

    } catch (_: Exception) {
    }
}

}