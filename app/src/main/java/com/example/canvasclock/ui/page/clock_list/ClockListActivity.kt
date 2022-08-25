package com.example.canvasclock.ui.page.clock_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.databinding.ActivityClockListBinding
import com.example.canvasclock.ui.recycler.adapter.ClockListPagingAdapter
import com.example.canvasclock.ui.recycler.decoration.Grid2Decoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockListActivity : BaseActivity<ActivityClockListBinding>(R.layout.activity_clock_list) {
    private val viewModel : ClockListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setObserver()
        setButton()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest { pagingData ->
                (binding.rvClockList.adapter as ClockListPagingAdapter).submitData(pagingData)
            }
        }
    }

    override fun setRecyclerView() {
        binding.rvClockList.layoutManager = GridLayoutManager(this, 2)
        binding.rvClockList.adapter = ClockListPagingAdapter()
        binding.rvClockList.addItemDecoration(Grid2Decoration(this))
    }

    override fun setButton() {
        binding.ivbtnBack.setOnClickListener {
            finish()
        }
    }
}