package com.strayAlpaca.domain.repository

import com.strayAlpaca.domain.models.FaqData

interface FaqRepository {
    suspend fun getFaqList() : List<FaqData>
}