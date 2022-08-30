package com.example.canvasclock.ui.page.clock_list

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.config.INTENT_KEY_CHANGED
import com.example.canvasclock.config.INTENT_KEY_CLOCK
import com.example.canvasclock.databinding.ActivityClockListBinding
import com.example.canvasclock.models.Crud
import com.example.canvasclock.ui.page.clock_detail.ClockDetailActivity
import com.example.canvasclock.ui.recycler.adapter.ClockListPagingAdapter
import com.example.canvasclock.ui.recycler.decoration.Grid2Decoration
import com.example.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockListActivity : BaseActivity<ActivityClockListBinding>(R.layout.activity_clock_list) {
    private val viewModel : ClockListViewModel by viewModels()
    private lateinit var modifyResult : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setObserver()
        setButton()

        modifyResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val changeCategory = result.data?.getSerializableExtra(INTENT_KEY_CHANGED)
                val clock = result.data?.getSerializableExtra(INTENT_KEY_CLOCK)

                if (changeCategory is Crud && clock is ClockData) {
                    when (changeCategory) {
                        Crud.DELETE -> {
                            viewModel.applyDelete(targetClock = clock)
                        }
                        Crud.UPDATE -> {
                            viewModel.applyUpdate(targetClock = clock)
                        }
                    }
                }
            }
        }
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
        binding.rvClockList.adapter = ClockListPagingAdapter(::goToDetailPage)
        binding.rvClockList.addItemDecoration(Grid2Decoration(this))
    }

    override fun setButton() {
        binding.ivbtnBack.setOnClickListener {
            finish()
        }
    }

    private fun goToDetailPage(clockData : ClockData){
        val intent = Intent(this, ClockDetailActivity::class.java)
        intent.putExtra(INTENT_KEY_CLOCK, clockData)
        modifyResult.launch(intent)
    }
}