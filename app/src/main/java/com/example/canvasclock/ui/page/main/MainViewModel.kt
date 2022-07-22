package com.example.canvasclock.ui.page.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.UseCaseGetMainClock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseGetMainClock : UseCaseGetMainClock
): ViewModel() {

    fun tryGetRecentWatch(amount : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            useCaseGetMainClock.execute(amount)
        }
    }
}