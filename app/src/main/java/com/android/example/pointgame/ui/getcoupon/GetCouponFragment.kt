package com.android.example.pointgame.ui.getcoupon

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.example.pointgame.R
import com.android.example.pointgame.databinding.FragmentGetCouponBinding
import com.android.example.pointgame.util.dp
import com.android.example.pointgame.util.sp
import com.google.android.material.button.MaterialButton


class GetCouponFragment : Fragment() {
    private var _binding: FragmentGetCouponBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // 共有ビューモデル
    private val sharedGetCouponViewModel: GetCouponViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val getCouponViewModel =
        //    ViewModelProvider(this)[GetCouponViewModel::class.java]

        // バインディング
        _binding = FragmentGetCouponBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // タイトル
        val textView: TextView = binding.fragmentGetCouponTitle
        sharedGetCouponViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // テーブル取得
        var tl: TableLayout = binding.fragmentGetCouponTableLayout

        // 行追加
        sharedGetCouponViewModel.coupons.observe(viewLifecycleOwner) {
            for (coupon in it) {
                addRow(tl, coupon)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addRow(tl: TableLayout, coupon: Coupon) {
        // 行用意
        var tr: TableRow = TableRow(requireContext())
        var dateTv: TextView = TextView(requireContext())
        var ll: LinearLayout = LinearLayout(requireContext())
        var couponTv: TextView = TextView(requireContext())
        var couponButton: Button = MaterialButton(requireContext())

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
            setMargins(couponButton.marginLeft, couponButton.marginTop, couponButton.marginRight, 4.dp)
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
            button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
            button.isEnabled = false
        }
        else {
            button.text = "クーポン使用"
            button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple_500)
        }
    }
}