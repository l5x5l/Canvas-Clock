package com.strayAlpaca.canvasclock.ui.page.clock_modify_shape

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strayAlpaca.canvasclock.config.MutableEventFlow
import com.strayAlpaca.canvasclock.config.asEventFlow
import com.strayAlpaca.canvasclock.models.ModifyClock
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.models.ClockPartData
import com.strayAlpaca.domain.usecase.UseCaseDeleteClockParts
import com.strayAlpaca.domain.usecase.UseCaseUpdateClock
import com.strayAlpaca.domain.usecase.UseCaseUpdateClockPart
import com.strayAlpaca.domain.utils.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ClockModifyViewModel @Inject constructor(
    private val useCaseUpdateClockPart: UseCaseUpdateClockPart,
    private val useCaseUpdateClock: UseCaseUpdateClock,
    private val useCaseDeleteClockParts: UseCaseDeleteClockParts
) : ViewModel() {

    private val _clockData = MutableStateFlow(ModifyClock.getInstance().getOriginalClock())
    val clockData = _clockData.asStateFlow()

    // 현재 ui 에 표시되는 시계 정보입니다.
    private val _saveModifiedClockResult = MutableEventFlow<Boolean>()
    val saveModifiedClockResult = _saveModifiedClockResult.asEventFlow()

    // 부품을 삭제할 경우, 삭제된 부품들의 목록이 존재합니다.
    private val removedClockPartList = arrayListOf<ClockPartData>()

    // 시계를 변경했는지 여부를 의미합니다.
    private var isChanged = false

    // 수정 대상 부품의 개수를 리턴합니다.
    fun getSelectedAmount() : Int {
        return clockData.value.clockPartList.filter { it.uiState.isSelected }.size
    }

    fun applyChangedClockPart() {
        isChanged = true
        _clockData.value = ModifyClock.getInstance().getMiddleSaveClock()
    }

    fun getIsChanged() = isChanged

    // 부품 변경 내역을 room db 에 저장합니다.
    fun saveModifiedClockParts(){
        viewModelScope.launch {
            try{
                clockData.value.lastModifiedTime = getCurrentTime()
                useCaseUpdateClock.execute(clockData.value)
                if (removedClockPartList.isNotEmpty()) useCaseDeleteClockParts.execute(removedClockPartList)
                useCaseUpdateClockPart.execute(clockData.value.clockPartList)
                ModifyClock.getInstance().initModifyClock(clockData.value)
                _saveModifiedClockResult.emit(true)
            } catch (e : Exception) {
                Log.e("update Clock", e.toString())
                _saveModifiedClockResult.emit(false)
            }
        }
    }

    // 부품을 제거합니다 (room db 에는 저장을 눌러야 반영됩니다)
    fun removeSelectParts() {
        removedClockPartList.addAll(clockData.value.clockPartList.filter { it.uiState.isSelected })
        val tempList = clockData.value.clockPartList.filter { !it.uiState.isSelected }
        val changedClock = ClockData.deepCopy(clockData.value)
        changedClock.clockPartList = ArrayList(tempList)
        ModifyClock.getInstance().setMiddleSaveClock(changedClock)
    }
}