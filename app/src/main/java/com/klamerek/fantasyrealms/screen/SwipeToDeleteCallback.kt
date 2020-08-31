package com.klamerek.fantasyrealms.screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.klamerek.fantasyrealms.R

/**
 * Generic Swipe implementation
 *
 * @property onSwipedAction     action to execute when an element is swapped
 * @constructor                 create a swiper by defining a context to load resources, directions of the swap movement and action when effectively swapped
 *
 * @param context               to load icon
 * @param swapDirections        direction of movement (ItemTouchHelper)
 */
class SwipeToDeleteCallback(context: Context, swapDirections: Int, val onSwipedAction: (Int) -> Unit) :
    ItemTouchHelper.SimpleCallback(0, swapDirections) {

    private val icon: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_36)!!
    private val background: ColorDrawable = ColorDrawable(Color.RED)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
    ;
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipedAction(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView
        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            icon.setBounds(0, 0, 0, 0)
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
        icon.draw(c)
    }


}