package com.example.canvasclock.ui.page.clock_modify_shape

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.canvasclock.R
import com.example.canvasclock.config.ADD_MODE
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.config.GlobalApplication
import com.example.canvasclock.databinding.ActivityClockModifyShapeBinding
import com.example.canvasclock.models.ModifyClock
import com.example.canvasclock.ui.custom_components.TwoButtonDialog
import com.example.canvasclock.ui.page.clock_modify_single_part.ClockModifySinglePartActivity
import com.example.canvasclock.ui.recycler.adapter.ClockPartAdapter
import com.example.canvasclock.ui.recycler.decoration.LinearVerticalDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockModifyShapeActivity : BaseActivity<ActivityClockModifyShapeBinding>(R.layout.activity_clock_modify_shape) {

    private val viewModel : ClockModifyViewModel by viewModels()
    private val confirmDialog : TwoButtonDialog by lazy { TwoButtonDialog() }
    private lateinit var modifyPartResult : ActivityResultLauncher<Intent>

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setButton()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.clockData.collect { clockData ->
                        (binding.rvPartList.adapter as ClockPartAdapter).applyClockPartList(clockData.clockPartList)
                        binding.viewClockShape.linkClockInfo(clockData.clockPartList)
                        binding.viewClockTime.linkClock(clockData)
                    }
                }

                launch {
                    viewModel.saveModifiedClockResult.collect { _ ->
                        GlobalApplication.isClockDBModified = true
                        val intent = Intent(this@ClockModifyShapeActivity, BaseActivity::class.java)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }

        confirmDialog.setFirstButton(buttonText = R.string.go_back)
        confirmDialog.setSecondButton(buttonText = R.string.cancel, buttonClickEvent = {
            finish()
        })

        modifyPartResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.applyChangedClockPart()
            }
        }
    }

    override fun setButton() {
        binding.tvbtnModify.setOnClickListener {
            if (viewModel.getSelectedAmount() >= 1) {
                // 사용 이유 : isSelected 의 변경으로 인해 다시 middleSaveClock 을 설정해야 함
                ModifyClock.getInstance().setMiddleSaveClock(viewModel.clockData.value)
                val intent = Intent(this, ClockModifySinglePartActivity::class.java)
                modifyPartResult.launch(intent)
            } else {
                showSimpleToast(getString(R.string.message_select_more_than_one_part))
            }
        }

        binding.ivbtnBack.setOnClickListener {
            if (viewModel.getIsChanged()) {
                confirmDialog.show(supportFragmentManager, "ConfirmDialog")
            } else {
                finish()
            }
        }

        binding.tvbtnSave.setOnClickListener {
            viewModel.saveModifiedClockParts()
        }
    }

    override fun setRecyclerView() {
        binding.rvPartList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPartList.adapter = ClockPartAdapter(addClickEvent = ::moveToClockPartModify)
        binding.rvPartList.addItemDecoration(LinearVerticalDecoration(this))
    }

    private fun moveToClockPartModify() {
        val intent = Intent(this, ClockModifySinglePartActivity::class.java)
        intent.putExtra(ADD_MODE, true)
        modifyPartResult.launch(intent)
    }
}