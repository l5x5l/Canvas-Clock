package com.example.canvasclock.ui.page.clock_modify_shape

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canvasclock.config.MutableEventFlow
import com.example.canvasclock.config.asEventFlow
import com.example.canvasclock.models.ModifyClock
import com.example.domain.usecase.UseCaseUpdateClock
import com.example.domain.usecase.UseCaseUpdateClockPart
import com.example.domain.utils.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ClockModifyViewModel @Inject constructor(
    private val useCaseUpdateClockPart: UseCaseUpdateClockPart,
    private val useCaseUpdateClock: UseCaseUpdateClock
) : ViewModel() {

    private val _clockData = MutableStateFlow(ModifyClock.getInstance().getOriginalClock())
    val clockData = _clockData.asStateFlow()

    private val _saveModifiedClockResult = MutableEventFlow<Boolean>()
    val saveModifiedClockResult = _saveModifiedClockResult.asEventFlow()

    private var isChanged = false

    fun getSelectedAmount() : Int {
        return clockData.value.clockPartList.filter { it.uiState.isSelected }.size
    }

    fun applyChangedClockPart() {
        isChanged = true
        _clockData.value = ModifyClock.getInstance().getMiddleSaveClock()
    }

    fun getIsChanged() = isChanged

    fun saveModifiedClockParts(){
        viewModelScope.launch {
            try{
                clockData.value.lastModifiedTime = getCurrentTime()
                useCaseUpdateClock.execute(clockData.value)
                useCaseUpdateClockPart.execute(clockData.value.clockPartList)
                ModifyClock.getInstance().initModifyClock(clockData.value)
                _saveModifiedClockResult.emit(true)
            } catch (e : Exception) {
                Log.e("update Clock", e.toString())
                _saveModifiedClockResult.emit(false)
            }
        }
    }
}