package com.strayAlpaca.canvasclock.ui.page.clock_detail.parts

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.BaseFragment
import com.strayAlpaca.canvasclock.databinding.FragmentClockDetailPartsBinding
import com.strayAlpaca.canvasclock.ui.recycler.adapter.ClockPartNormalAdapter
import com.strayAlpaca.canvasclock.ui.recycler.decoration.LinearVerticalDecoration
import com.strayAlpaca.domain.models.ClockPartData

class ClockDetailPartsFragment : BaseFragment<FragmentClockDetailPartsBinding>(FragmentClockDetailPartsBinding::bind, R.layout.fragment_clock_detail_parts) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    override fun setRecyclerView(){
        binding.rvPartList.layoutManager = LinearLayoutManager(requireContext() ,LinearLayoutManager.VERTICAL, false)
        binding.rvPartList.adapter = ClockPartNormalAdapter()
        binding.rvPartList.addItemDecoration(LinearVerticalDecoration(requireContext()))
    }

    fun setClockPartData(clockPartList : ArrayList<ClockPartData>) {
        (binding.rvPartList.adapter as ClockPartNormalAdapter).setClockPart(clockPartList)
    }
}