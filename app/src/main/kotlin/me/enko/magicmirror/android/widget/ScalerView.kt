package me.enko.magicmirror.android.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

class ScalerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {
    private var child: View? = null

    @SuppressLint("RtlHardcoded")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val myWidth = measuredWidth
        val myHeight = measuredHeight
        val child = this.child
        if (child != null && child.visibility == View.VISIBLE) {
            val childWidth = child.measuredWidth.toFloat()
            val childHeight = child.measuredHeight.toFloat()

            val horizontalScale = myWidth / childWidth
            val verticalScale = myHeight / childHeight
            val scale = Math.min(horizontalScale, verticalScale)
            child.scaleX = scale
            child.scaleY = scale

            var gravity = (child.layoutParams as LayoutParams).gravity
            if (gravity == -1) {
                gravity = Gravity.TOP or Gravity.START
            }

            val layoutDirection = layoutDirection
            val absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection)
            val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK

            when (absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
                Gravity.CENTER_HORIZONTAL -> child.pivotX = childWidth / 2
                Gravity.RIGHT -> child.pivotX = childWidth
                else -> child.pivotX = 0f
            }

            when (verticalGravity) {
                Gravity.TOP -> child.pivotY = 0f
                Gravity.CENTER_VERTICAL -> child.pivotY = childHeight / 2
                else -> child.pivotY = childHeight
            }
        }
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        if (this.child != null) {
            throw IllegalStateException("Only one child supported")
        }
        this.child = child
    }

    override fun onViewRemoved(child: View) {
        super.onViewRemoved(child)
        this.child = null
    }
}