package com.strayAlpaca.canvasclock.ui.recycler.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.util.dpToPx

class LinearVerticalDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val interval = dpToPx(context = context, 12)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = interval
    }
}