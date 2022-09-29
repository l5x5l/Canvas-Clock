package com.strayAlpaca.canvasclock.ui.page.clock_detail.modify

import android.os.Bundle
import android.view.View
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.BaseFragment
import com.strayAlpaca.canvasclock.databinding.FragmentClockDetailModifyBinding
import com.strayAlpaca.canvasclock.ui.page.clock_detail.ClockDetailActivity
import com.strayAlpaca.domain.models.ClockPartData

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