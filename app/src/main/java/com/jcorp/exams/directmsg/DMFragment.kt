package com.jcorp.exams.directmsg

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.jcorp.exams.databinding.FragmentDirectMessageBinding
import com.jcorp.exams.directmsg.adapter.PagerAdapter
import com.jcorp.exams.mViewModel


class DMFragment : Fragment() {
    private val viewModel by activityViewModels<mViewModel>()
    private lateinit var binding : FragmentDirectMessageBinding
    private lateinit var adapter : PagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDirectMessageBinding.inflate(inflater, container, false)

        setPager()
        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.pagerPosition.observe(requireActivity(), Observer {
            Log.d("----", "pagerPosition: $it")
            binding.pager.setCurrentItem(it, true)
        })
    }

    private fun setPager() {
        val list = mutableListOf<Fragment>(DMListView(), MsgFrameFragment(),DmThirdFragment())
        adapter = PagerAdapter(requireActivity())
        adapter.setItem(list)

        binding.pager.adapter = adapter
        Log.d("----", "setPager: ${binding.pager.currentItem}")


        binding.pager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2
        }

        val offsetPx = 50.dpToPx(resources.displayMetrics)
        binding.pager.setPadding(0, 0, offsetPx, 0)
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            /*override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                viewModel.pagerPosition.value = position
                viewModel.changeBottomViewState()

                Log.d("----", "onPageScrolled: $position")
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }*/
            override fun onPageSelected(position: Int) {

                viewModel.pagerPosition.value = position
                viewModel.changeBottomViewState()

                Log.d("----", "onPageScrolled: $position")

                super.onPageSelected(position)
            }
        })

        // 페이지간 마진 크게하려면 pageMarginPx 증가
        val pageMarginPx = 8.dpToPx(resources.displayMetrics)
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        binding.pager.setPageTransformer(marginTransformer)

        viewModel.pagerPosition.observe(requireActivity(), Observer {
            when(it) {
                0 -> {
                    binding.pager.setPadding(0, 0, offsetPx, 0)
                }
                1 -> {
                    binding.pager.setPadding(0, 0, 0, 0)
                }
                2 -> {
                    binding.pager.setPadding(offsetPx, 0, 0, 0)
                }
            }
        })
    }

    fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()
}