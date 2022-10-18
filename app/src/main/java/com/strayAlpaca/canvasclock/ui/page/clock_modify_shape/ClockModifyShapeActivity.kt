package com.strayAlpaca.canvasclock.ui.page.clock_modify_shape

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.*
import com.strayAlpaca.canvasclock.databinding.ActivityClockModifyShapeBinding
import com.strayAlpaca.canvasclock.models.ModifyClock
import com.strayAlpaca.canvasclock.ui.custom_components.TwoButtonDialog
import com.strayAlpaca.canvasclock.ui.page.clock_modify_handle.ClockModifyHandleActivity
import com.strayAlpaca.canvasclock.ui.page.clock_modify_single_part.ClockModifySinglePartActivity
import com.strayAlpaca.canvasclock.ui.recycler.adapter.ClockPartAdapter
import com.strayAlpaca.canvasclock.ui.recycler.decoration.LinearVerticalDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockModifyShapeActivity : BaseActivity<ActivityClockModifyShapeBinding>(R.layout.activity_clock_modify_shape) {

    private val viewModel : ClockModifyViewModel by viewModels()
    private val confirmDialog : TwoButtonDialog by lazy { TwoButtonDialog() }
    private lateinit var modifyPartResult : ActivityResultLauncher<Intent>

    private var isCreateMode = false

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setButton()

        isCreateMode = intent.getBooleanExtra(INTENT_KEY_CREATE_CLOCK, false)
        if (isCreateMode) {
            binding.tvbtnSave.text = getString(R.string.move_to_next_step)
            confirmDialog.setMainMessage(R.string.message_confirm_cancellation_create_clock)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.clockData.collect { clockData ->
                        (binding.rvPartList.adapter as ClockPartAdapter).applyClockPartList(clockData.clockPartList)
                        binding.viewClockShape.linkClockInfo(clockData.clockPartList)
                        binding.viewClockTime.linkClock(clockData)

                        binding.viewClockShape.invalidateClock()
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

        // 시계를 수정할 때와 시계를 생성할 때가 다름
        binding.ivbtnBack.setOnClickListener {
            if (isCreateMode) {
                confirmDialog.show(supportFragmentManager, "ConfirmDialog")
            }
            else {
                if (viewModel.getIsChanged()) {
                    confirmDialog.show(supportFragmentManager, "ConfirmDialog")
                } else {
                    finish()
                }
            }
        }

        // 시계를 수정할 때와 시계를 생성할 때가 다름
        binding.tvbtnSave.setOnClickListener {
            val numberOfParts = viewModel.clockData.value.clockPartList.size
            if (numberOfParts == 0) {
                showSimpleToast(getString(R.string.message_clock_must_have_at_least_one_part))
            } else {
                if (isCreateMode){
                    ModifyClock.getInstance().initModifyClock(viewModel.clockData.value)
                    val intent = Intent(this, ClockModifyHandleActivity::class.java)
                    intent.putExtra(INTENT_KEY_CLOCK, isCreateMode)
                    startActivity(intent)
                } else {
                    viewModel.saveModifiedClockParts()
                }
            }
        }

        binding.tvbtnDelete.setOnClickListener {
            if (viewModel.getSelectedAmount() >= 1) {
                viewModel.removeSelectParts()
                viewModel.applyChangedClockPart()
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
        val intent = Intent(this, ClockModifySinglePartActivity::class.java)
        intent.putExtra(ADD_MODE, true)
        modifyPartResult.launch(intent)
    }
}