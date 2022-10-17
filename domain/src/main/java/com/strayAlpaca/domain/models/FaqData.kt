package com.strayAlpaca.domain.models

data class FaqData (val title : String = "", val answer : String = "", val images : List<String>? = null, val shortCut : ShortCut? = null)