package com.redmechax00.astonintensive6.presentation.contacts.adapter

import android.content.Context
import android.graphics.Rect
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.redmechax00.astonintensive6.R

class ContactsItemDecoration(context: Context) : ItemDecoration() {

    private var divider: Drawable
    private val rect = Rect()
    private val itemPadding = context.resources.getDimension(R.dimen.item_contact_padding).toInt()
    private val dividerPadding =
        context.resources.getDimension(R.dimen.item_contact_divider_padding).toInt()

    init {
        val defDrawable = ColorDrawable(Color.TRANSPARENT)
        divider = ContextCompat.getDrawable(context, R.drawable.contact_item_divider) ?: defDrawable
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect[itemPadding, itemPadding, itemPadding] = itemPadding
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager != null) {
            drawHorizontalDivider(c, parent)
        }
    }

    private fun drawHorizontalDivider(c: Canvas, parent: RecyclerView) {
        c.save()
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, rect)

            val left = child.left + child.paddingStart + dividerPadding
            val right = child.right - child.paddingEnd - dividerPadding
            val bottom = child.bottom + itemPadding
            val top = bottom - divider.intrinsicHeight

            divider.setBounds(
                left,
                top,
                right,
                bottom
            )
            divider.draw(c)
        }
        c.restore()
    }
}