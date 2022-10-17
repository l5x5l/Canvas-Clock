package com.strayAlpaca.canvasclock.ui.page.faq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strayAlpaca.domain.models.FaqData
import com.strayAlpaca.domain.usecase.UseCaseFaq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(
    private val useCaseFaq: UseCaseFaq
) : ViewModel()  {
    private val _faqList = MutableStateFlow<List<FaqData>>(listOf())
    val faqList = _faqList.asStateFlow()

    fun getFaqList() {
        viewModelScope.launch {
            _faqList.value = useCaseFaq.getFaqList()
        }
    }
}