package com.example.androidpracticumcustomview.ui.theme

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.view.children

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    var durationAlphaAnimated: Long = 2000L,
    var durationTransformAnimated: Long = 5000L
) : FrameLayout(context, attrs) {

    init {
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        children.forEach {
            val lp = it.layoutParams as? MarginLayoutParams
            val heightSpec = getChildMeasureSpec(
                heightMeasureSpec,
                (lp?.topMargin ?: 0) + (lp?.bottomMargin
                    ?: 0) + this.paddingTop + this.paddingBottom,
                WRAP_CONTENT
            )
            val widthSpec = getChildMeasureSpec(
                widthMeasureSpec,
                (lp?.marginStart ?: 0) + (lp?.marginEnd
                    ?: 0) + this.paddingStart + this.paddingEnd,
                WRAP_CONTENT
            )
            it.measure(widthSpec, heightSpec)
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val width = child.measuredWidth
            val height = child.measuredHeight
            val childTop = ((bottom - top) - height) / 2
            val childLeft = left + (right - left - width) / 2
            child.layout(
                childLeft,
                childTop,
                childLeft + width,
                childTop + width
            )
            if (child.alpha == 0f) {
                val translate = (this.measuredHeight / 4).toFloat()
                child.animate()
                    .alpha(1f)
                    .setDuration(durationAlphaAnimated).start()
                child.animate()
                    .translationY(if (i == 0) -translate else translate)
                    .setDuration(durationTransformAnimated).start()
            }

        }
    }

    override fun addView(child: View) {
        if (this.childCount > 2) throw IllegalStateException("Не может быть больше 2-х Вью")
        child.apply {
            alpha = 0f
        }
        this.addView(child, childCount)
    }
}