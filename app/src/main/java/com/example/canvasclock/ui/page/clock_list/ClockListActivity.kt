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
import com.example.canvasclock.config.INTENT_KEY_CLOCK_MODE
import com.example.canvasclock.databinding.ActivityClockListBinding
import com.example.canvasclock.models.Crud
import com.example.canvasclock.ui.page.clock_detail.ClockDetailActivity
import com.example.canvasclock.ui.page.clock_mode.ClockModeActivity
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

    // 시계목록 화면의 경우, 시계 모드에 사용될 시계를 선택하는 경우와 일반적인 시계 리스트를 조회하는 경우의 2가지 케이스를 가집니다.
    // isClockMode 가 true 이면 시계모드에 사용될 시계를 선택하는 경우를 의미합니다.
    private var isClockMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setObserver()
        setButton()

        isClockMode = intent.getBooleanExtra(INTENT_KEY_CLOCK_MODE, false)
        if (isClockMode) {  // 시계모드에 사용될 시계를 선택하는 케이스의 경우, 타이틀 텍스트를 "시계 목록"에서 "시계 선택"으로 변경합니다.
            binding.tvTitle?.text = getString(R.string.choose_clock)
        }

        // 이 다음 페이지(시계 상세 화면)에서 시계를 삭제하거나 수정한 경우, 해당 내용을 시계 목록에 반영하기 위한 부분입니다.
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
        binding.rvClockList.adapter = ClockListPagingAdapter(::itemClickEvent)
        binding.rvClockList.addItemDecoration(Grid2Decoration(this))
    }

    override fun setButton() {
        binding.ivbtnBack.setOnClickListener {
            finish()
        }
    }

    // 시계 목록 recyclerView 의 아이템 클릭 이벤트입니다.
    private fun itemClickEvent(clockData : ClockData){
        if (isClockMode) {
            val intent = Intent(this, ClockModeActivity::class.java)
            intent.putExtra(INTENT_KEY_CLOCK, clockData)
            startActivity(intent)
        } else {
            val intent = Intent(this, ClockDetailActivity::class.java)
            intent.putExtra(INTENT_KEY_CLOCK, clockData)
            modifyResult.launch(intent)
        }
    }
}