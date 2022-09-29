package com.strayAlpaca.domain.models

import java.io.Serializable

data class UiStateData(
    var isSelected : Boolean = false,
    var isNew : Boolean = false
) : Serializable
