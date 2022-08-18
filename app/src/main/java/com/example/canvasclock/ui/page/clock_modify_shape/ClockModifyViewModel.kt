package com.example.canvasclock.ui.page.clock_modify_shape

import androidx.lifecycle.ViewModel
import com.example.canvasclock.models.ModifyClock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ClockModifyViewModel @Inject constructor() : ViewModel() {

    private val _clockData = MutableStateFlow(ModifyClock.getInstance().getOriginalClock())
    val clockData = _clockData.asStateFlow()

    private var isChanged = false

    fun getSelectedAmount() : Int {
        return clockData.value.clockPartList.filter { it.uiState.isSelected }.size
    }

    fun applyChangedClockPart() {
        isChanged = true
        _clockData.value = ModifyClock.getInstance().getMiddleSaveClock()
    }

    fun getIsChanged() = isChanged
}