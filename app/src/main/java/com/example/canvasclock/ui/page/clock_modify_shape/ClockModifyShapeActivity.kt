package com.example.canvasclock.ui.page.clock_modify_shape

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.databinding.ActivityClockModifyShapeBinding
import com.example.canvasclock.models.ModifyClock
import com.example.canvasclock.ui.page.clock_modify_single_part.ClockModifySinglePartActivity
import com.example.canvasclock.ui.recycler.adapter.ClockPartAdapter
import com.example.canvasclock.ui.recycler.decoration.LinearVerticalDecoration
import kotlinx.coroutines.launch

class ClockModifyShapeActivity : BaseActivity<ActivityClockModifyShapeBinding>(R.layout.activity_clock_modify_shape) {

    private val viewModel : ClockModifyViewModel by viewModels()

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setButton()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clockData.collect { clockData ->
                    (binding.rvPartList.adapter as ClockPartAdapter).applyClockPartList(clockData.clockPartList)
                    binding.viewClockShape.linkClockInfo(clockData.clockPartList)
                    binding.viewClockTime.linkClock(clockData)
                }
            }
        }

    }

    override fun setButton() {
        binding.tvbtnModify.setOnClickListener {
            if (viewModel.getSelectedAmount() >= 1) {
                ModifyClock.getInstance().setMiddleSaveClock(viewModel.clockData.value)
                val intent = Intent(this, ClockModifySinglePartActivity::class.java)
                startActivity(intent)
            } else {
                showSimpleToast(getString(R.string.message_select_more_than_one_part))
            }
        }
    }

    override fun setRecyclerView() {
        binding.rvPartList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPartList.adapter = ClockPartAdapter(addClickEvent = ::moveToClockPartModify)
        binding.rvPartList.addItemDecoration(LinearVerticalDecoration(this))
    }

    private fun moveToClockPartModify() {
        Log.d("!!!!", viewModel.clockData.value.clockPartList.toString())
    }
}