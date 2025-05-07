package com.example.androidpracticumcustomview.ui.theme

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.view.children
import com.example.androidpracticumcustomview.R

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private var durationAlphaAnimated: Int = 2000
    private var durationTransformAnimated: Int = 5000

    init {
        setWillNotDraw(false)
        val arrayProperties = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomContainer,
            0,
            R.style.CustomContainerDef
        )
        try {
            durationAlphaAnimated =
                arrayProperties.getInt(R.styleable.CustomContainer_duration_alpha_animated, 2000)
            durationTransformAnimated =
                arrayProperties.getInt(R.styleable.CustomContainer_duration_transformY_animated, 5000)
        } finally {
            arrayProperties.recycle()
        }
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

            val translate = (this.measuredHeight / 4).toFloat()

            child.animate()
                .alpha(1f)
                .setDuration(durationAlphaAnimated.toLong()).start()
            child.animate()
                .translationY(if (i == 0) -translate else translate)
                .setDuration(durationTransformAnimated.toLong()).start()

            // Вариант с AnimatorSet

//            val animate1 = ObjectAnimator.ofFloat(child, "alpha",  1f)
//                .setDuration(durationAlphaAnimated.toLong())
//            val animate2 = ObjectAnimator.ofFloat(child, "translationY", if (i == 0) -translate else translate)
//                .setDuration(durationTransformAnimated.toLong())
//
//            AnimatorSet().apply {
//                playTogether(animate1, animate2)
//                start()
//            }
        }
    }

    override fun addView(child: View) {
        if (this.childCount > 2) throw IllegalStateException("Не может быть больше 2-х Вью")
        child.apply {
            alpha = 0f
        }
        super.addView(child)
    }
}