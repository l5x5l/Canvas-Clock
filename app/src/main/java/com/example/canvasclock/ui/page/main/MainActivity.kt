package com.example.canvasclock.ui.page.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.canvasclock.R
import com.example.canvasclock.config.*
import com.example.canvasclock.databinding.ActivityMainBinding
import com.example.canvasclock.models.ModifyClock
import com.example.canvasclock.ui.page.clock_detail.ClockDetailActivity
import com.example.canvasclock.ui.page.clock_list.ClockListActivity
import com.example.canvasclock.ui.page.clock_modify_shape.ClockModifyShapeActivity
import com.example.canvasclock.ui.recycler.adapter.MainClockAdapter
import com.example.canvasclock.ui.recycler.decoration.Grid3Decoration
import com.example.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setButton()
        setRecyclerView()

        viewModel.startTimer()
        viewModel.tryGetRandomClock(1)
        viewModel.tryGetRecentModifyClock(5)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.mainClockState.collect { eventState ->
                        if (eventState.isLoading) {
                            binding.progressLoadingMainClock.visibility = View.VISIBLE
                        } else {
                            binding.progressLoadingMainClock.visibility = View.GONE
                            if (eventState.value != null) {
                                binding.viewClockShape.linkClockInfo(eventState.value[0].clockPartList)
                                binding.viewClockTime.linkClock(eventState.value[0])
                                binding.viewClockShape.invalidateClock()
                                binding.viewIconClock.linkClockInfo(eventState.value[0].clockPartList)
                                binding.viewIconClock.invalidateClock()
                            }
                        }
                    } // viewModel.mainClockState.collect
                }

                launch {
                    viewModel.clockListState.collect { eventState ->
                        if (!eventState.isLoading) {
                            if (eventState.value != null) {
                                (binding.rvClockList.adapter as MainClockAdapter).setClockList(eventState.value)
                            }
                        }
                    } // viewModel.clockListState.collect
                }
            } // repeatOnLifecycle
        } // lifecycleScope
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopTimer()
    }

    override fun onRestart() {
        super.onRestart()

        if (GlobalApplication.isClockDBModified) {
            GlobalApplication.isClockDBModified = false
            viewModel.tryGetRandomClock(1)
            viewModel.tryGetRecentModifyClock(5)
        }

        viewModel.startTimer()
    }


    override fun setButton() {
        // 이 부분 다시 수정 필요
        binding.viewClockShape.setOnClickListener {
            try {
                val intent = Intent(this, ClockDetailActivity::class.java)
                intent.putExtra(INTENT_KEY_CLOCK, viewModel.mainClockState.value.value?.get(0))
                startActivity(intent)
            } catch (e : Exception) { // mainClockState 가 아직 로딩되지 않은 경우에 버튼 클릭 (room db 라 발생확률 낮음)
                showSimpleToast(getString(R.string.message_not_loading_yet))
            }
        }

        binding.layoutbtnAddClock.setOnClickListener {
            val intent = Intent(this, ClockModifyShapeActivity::class.java)
            intent.putExtra(INTENT_KEY_CREATE_CLOCK, true)
            ModifyClock.getInstance().initModifyClock(ClockData())
            startActivity(intent)
        }

        binding.layoutbtnClockMode.setOnClickListener {
            val intent = Intent(this, ClockListActivity::class.java)
            intent.putExtra(INTENT_KEY_CLOCK_MODE, true)
            startActivity(intent)
        }
    }

    override fun setRecyclerView() {
        binding.rvClockList.layoutManager = GridLayoutManager(this, 3)
        binding.rvClockList.adapter = MainClockAdapter()
        binding.rvClockList.addItemDecoration(Grid3Decoration(this))
    }

    override fun preLoad() {

    }

}