package com.strayAlpaca.canvasclock.ui.recycler.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.util.dpToPx

class GridDecoration(context : Context, private val gridColumn : Int, itemInterval : Int = 10) : RecyclerView.ItemDecoration() {
    private val pxInterval = dpToPx(context, itemInterval)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        // 한 아이템에서 설정한 outRect 의 총합이 모든 position 에서 일치해야
        // 아이템의 크기가 position 에 관계없이 일치해진다.
        when (position % gridColumn) {
            0 -> {
                outRect.right = pxInterval
            }
            gridColumn - 1 -> {
                outRect.left = pxInterval
            }
            else -> {
                outRect.right = pxInterval / 2
                outRect.left = pxInterval / 2
            }
        }

        outRect.bottom = pxInterval
    }

}