package com.example.canvasclock.ui.page.clock_detail.modify

import android.os.Bundle
import android.view.View
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseFragment
import com.example.canvasclock.databinding.FragmentClockDetailModifyBinding
import com.example.canvasclock.ui.page.clock_detail.ClockDetailActivity
import com.example.domain.models.ClockPartData

class ClockDetailModifyFragment : BaseFragment<FragmentClockDetailModifyBinding>(FragmentClockDetailModifyBinding::bind, R.layout.fragment_clock_detail_modify) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutbtnModifyHandle.setOnClickListener {

        }
        binding.layoutbtnModifyShape.setOnClickListener {
            (activity as ClockDetailActivity).moveToModifyShape()
        }
        binding.layoutbtnModifyHandle.setOnClickListener {
            (activity as ClockDetailActivity).moveToModifyHandle()
        }
    }

    fun changeClockShape(clockPartList : ArrayList<ClockPartData>) {
        binding.viewClockShape.linkClockInfo(clockPartList)
        binding.viewClockShape.invalidateClock()
    }
}