package com.android.example.pointgame.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.example.pointgame.databinding.FragmentHomePagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomePagerFragment : Fragment() {
    private var _binding: FragmentHomePagerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homePagerViewModel =
//            ViewModelProvider(this).get(HomePagerViewModel::class.java)

        _binding = FragmentHomePagerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // ページインスタンスを用意
        val pagerAdapter = PagerAdapter(requireActivity())
        // セット
        binding.fragmentHomePager.adapter = pagerAdapter

        TabLayoutMediator(binding.fragmentHomeTabLayout, binding.fragmentHomePager) { _, _ -> }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        // ページ数を取得
        override fun getItemCount(): Int = 2

        // スワイプ位置を引数にしてFragment生成する
        override fun createFragment(position: Int): Fragment =
            HomeContentFragment(position, binding)
    }
}