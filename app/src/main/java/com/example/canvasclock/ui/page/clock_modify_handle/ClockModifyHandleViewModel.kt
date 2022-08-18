package com.example.canvasclock.ui.page.clock_modify_handle

import androidx.lifecycle.ViewModel
import com.example.canvasclock.config.MutableEventFlow
import com.example.canvasclock.config.asEventFlow
import com.example.canvasclock.models.ModifyClock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ClockModifyHandleViewModel @Inject constructor() : ViewModel() {
    private val _clockData = MutableStateFlow(ModifyClock.getInstance().getOriginalClock())
    val clockData = _clockData.asStateFlow()

    private val _saveModifiedClockResult = MutableEventFlow<Int>()
    val saveModifiedClockResult = _saveModifiedClockResult.asEventFlow()

    private var isChanged = false
}