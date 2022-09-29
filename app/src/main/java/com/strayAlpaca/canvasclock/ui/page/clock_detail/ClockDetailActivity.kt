package com.strayAlpaca.canvasclock.ui.page.clock_detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.*
import com.strayAlpaca.canvasclock.databinding.ActivityClockDetailBinding
import com.strayAlpaca.canvasclock.models.Crud
import com.strayAlpaca.canvasclock.models.ModifyClock
import com.strayAlpaca.canvasclock.ui.custom_components.TwoButtonDialog
import com.strayAlpaca.canvasclock.ui.page.clock_detail.modify.ClockDetailModifyFragment
import com.strayAlpaca.canvasclock.ui.page.clock_detail.parts.ClockDetailPartsFragment
import com.strayAlpaca.canvasclock.ui.page.clock_modify_handle.ClockModifyHandleActivity
import com.strayAlpaca.canvasclock.ui.page.clock_modify_shape.ClockModifyShapeActivity
import com.strayAlpaca.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockDetailActivity : BaseActivity<ActivityClockDetailBinding>(R.layout.activity_clock_detail) {

    private val viewModel : ClockDetailViewModel by viewModels()
    private val partsFragment : ClockDetailPartsFragment by lazy { ClockDetailPartsFragment() }
    private val modifyFragment : ClockDetailModifyFragment by lazy { ClockDetailModifyFragment() }
    private lateinit var currentFragment : BaseFragment<*>

    private lateinit var modifyResult : ActivityResultLauncher<Intent>

    private val confirmDialog : TwoButtonDialog by lazy { TwoButtonDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clock = intent.getSerializableExtra(INTENT_KEY_CLOCK) as ClockData
        viewModel.setClockData(clockData = clock)

        initFragment()
        setButton()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.mainClockState.collect { eventState ->
                        if (!eventState.isLoading) {
                            binding.progressLoadingMainClock.visibility = View.GONE
                            if (eventState.value != null) {
                                partsFragment.setClockPartData(eventState.value.clockPartList)
                                modifyFragment.changeClockShape(eventState.value.clockPartList)
                                binding.viewClockTime.linkClock(eventState.value)
                                binding.viewClockShape.linkClockInfo(eventState.value.clockPartList)
                                binding.viewClockShape.invalidateClock()
                            }
                        } else {
                            binding.progressLoadingMainClock.visibility = View.VISIBLE
                        }
                    }
                }

                launch {
                    viewModel.checkClockCountEvent.collect { clockCount ->
                        if (clockCount <= 1) {
                            showSimpleToast(getString(R.string.message_can_delete_clock_only_more_than_one))
                        } else {
                            confirmDialog.show(supportFragmentManager, "ConfirmDialog")
                        }
                    }
                }

                launch {
                    viewModel.deleteClockEvent.collect { deleteSuccess ->
                        if (deleteSuccess) {
                            showSimpleToast(getString(R.string.message_deletion_success))
                            GlobalApplication.isClockDBModified = true
                            val intent = Intent(this@ClockDetailActivity, BaseActivity::class.java)
                            intent.putExtra(INTENT_KEY_CLOCK, ModifyClock.getInstance().getOriginalClock())
                            intent.putExtra(INTENT_KEY_CHANGED, Crud.DELETE)
                            setResult(RESULT_OK, intent)
                            finish()
                        } else {
                            showSimpleToast(getString(R.string.message_deletion_failure))
                        }
                    }
                }
            }
        }

        confirmDialog.setFirstButton(R.string.go_back)
        confirmDialog.setSecondButton(R.string.delete, buttonClickEvent = {
            viewModel.deleteClockEvent()
        })
        confirmDialog.setMainMessage(R.string.message_confirm_delete_clock)

        modifyResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.applyModifyClockData(ModifyClock.getInstance().getOriginalClock())
                viewModel.setIsUpdated(value = true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ModifyClock.clearInstance()
    }

    override fun setButton() {
        binding.ivbtnBack.setOnClickListener {
            if (currentFragment == modifyFragment) {
                supportFragmentManager.beginTransaction().hide(currentFragment).show(partsFragment).commit()
                currentFragment = partsFragment
                binding.tvbtnModify.visibility = View.VISIBLE
                binding.tvbtnDelete.visibility = View.VISIBLE
            } else {
                finish()
            }
        }

        binding.tvbtnModify.setOnClickListener {
            supportFragmentManager.beginTransaction().hide(currentFragment).show(modifyFragment).commit()
            currentFragment = modifyFragment
            it.visibility = View.INVISIBLE
            binding.tvbtnDelete.visibility = View.INVISIBLE
        }

        binding.tvbtnDelete.setOnClickListener {
            viewModel.checkClockCount()
        }
    }

    override fun finish() {
        if (viewModel.getIsUpdated()) {
            val intent = Intent(this@ClockDetailActivity, BaseActivity::class.java)
            intent.putExtra(INTENT_KEY_CLOCK, ModifyClock.getInstance().getOriginalClock())
            intent.putExtra(INTENT_KEY_CHANGED, Crud.UPDATE)
            setResult(RESULT_OK, intent)
            super.finish()
        } else {
            super.finish()
        }
    }

    private fun initFragment(){
        supportFragmentManager.beginTransaction().add(binding.layoutFragment.id, modifyFragment).hide(modifyFragment).add(binding.layoutFragment.id, partsFragment).commit()
        currentFragment = partsFragment
    }

    fun moveToModifyShape(){
        val intent = Intent(this, ClockModifyShapeActivity::class.java)
        ModifyClock.getInstance().setOriginalClock(viewModel.mainClockState.value.value!!)
        modifyResult.launch(intent)
    }

    fun moveToModifyHandle(){
        val intent = Intent(this, ClockModifyHandleActivity::class.java)
        ModifyClock.getInstance().setOriginalClock(viewModel.mainClockState.value.value!!)
        modifyResult.launch(intent)
    }
}