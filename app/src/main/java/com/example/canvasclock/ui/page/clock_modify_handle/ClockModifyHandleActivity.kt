package com.example.canvasclock.ui.page.clock_modify_handle

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.databinding.ActivityClockModifyHandleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockModifyHandleActivity : BaseActivity<ActivityClockModifyHandleBinding>(R.layout.activity_clock_modify_handle) {
    private val viewModel : ClockModifyHandleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.clockData.collect { clockData ->
                        binding.viewClockShape.linkClockInfo(clockData.clockPartList)
                        binding.viewClockTime.linkClock(clockData)
                    }
                }

                launch {
                    viewModel.saveModifiedClockResult.collect {
                        showSimpleToast("완료되었습니다.")
                        finish()
                    }
                }
            }
        }
    }
}