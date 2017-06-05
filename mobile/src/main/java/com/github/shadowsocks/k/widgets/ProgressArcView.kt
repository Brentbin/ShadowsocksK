/**
 * Copyright (C) 2017 lhyz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.shadowsocks.k.widgets

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.properties.Delegates

/**
 * hello,kotlin
 * Created by lhyz on 2017/5/31.
 */
class ProgressArcView(context: Context) : View(context) {

    private val TAG = ProgressArcView::class.java.canonicalName

    private var mPaint: Paint by Delegates.notNull()
    private var mRect: RectF by Delegates.notNull()
    private var mStartAngle: Float by Delegates.notNull()
    private var mSweepAngle: Float by Delegates.notNull()
    var strokeWidth: Float = 8f
    private var animation: ValueAnimator by Delegates.notNull()

    var listener: ArchListener? = null

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = 0xFFF97A05.toInt()
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        mPaint.strokeCap = Paint.Cap.BUTT

        mRect = RectF()

        mStartAngle = -90f
        mSweepAngle = 0f

        animation = ValueAnimator.ofFloat(-90f, 270f)
        animation.duration = 3000
        animation.interpolator = DecelerateInterpolator()
        animation.addUpdateListener { anim ->
            val v = anim?.animatedValue as Float
            mStartAngle = v
            mSweepAngle = v + 90f
            Log.e(TAG, "" + v)
            invalidate()
        }
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                listener?.onEnd()
            }

            override fun onAnimationCancel(animation: Animator?) {
                listener?.onCancel()
            }

            override fun onAnimationStart(animation: Animator?) {
                listener?.onStart()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        val height = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        mRect.set(0f + strokeWidth / 2, 0f + strokeWidth / 2, width - strokeWidth / 2, height - strokeWidth / 2)
        mPaint.strokeWidth = strokeWidth
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint)
        Log.e(TAG, "draw")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        playAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    fun playAnimation() {
        animation.start()
    }

    fun stopAnimation() {
        animation.cancel()
    }


    interface ArchListener {
        fun onCancel()
        fun onEnd()
        fun onStart()
    }
}