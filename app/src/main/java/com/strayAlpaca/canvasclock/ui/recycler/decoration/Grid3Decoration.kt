package com.strayAlpaca.canvasclock.ui.recycler.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.util.dpToPx

class Grid3Decoration(context : Context) : RecyclerView.ItemDecoration() {
    private val interval = dpToPx(context = context, 10)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)

        if (position % 3 == 0) {
            outRect.right = interval
        } else if (position % 3 == 1) {
            outRect.right = interval / 2
            outRect.left = interval / 2
        } else {
            outRect.left = interval
        }
        outRect.bottom = interval
    }
}