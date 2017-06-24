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
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.shadowsocks.k.R
import kotlin.properties.Delegates

/**
 * hello,kotlin
 * Created by lhyz on 2017/5/30.
 *
 *  A circular loader may be integrated with a floating action button.
 *
 * implement of https://material.io/guidelines/components/progress-activity.html#progress-activity-types-of-indicators
 *
 * TODO implement indeterminate progress style
 */

class CircularLoader(context: Context, attrs: AttributeSet) :
        FrameLayout(context, attrs) {

    private var isMeasured = false
    private var fab: ImageView by Delegates.notNull()
    private var arc: ArcView by Delegates.notNull()
    private var status: StatusView by Delegates.notNull()

    override fun onFinishInflate() {
        super.onFinishInflate()

        fab = getChildAt(0) as ImageView
        val params = fab.layoutParams as LayoutParams
        params.gravity = Gravity.CENTER

        arc = ArcView(context)
        addView(arc, LayoutParams(144, 144, Gravity.CENTER))
        ViewCompat.setElevation(arc, ViewCompat.getElevation(fab) + 1)

        status = StatusView(context)
        addView(status, LayoutParams(144, 144, Gravity.CENTER))
        ViewCompat.setElevation(status, ViewCompat.getElevation(fab) + 2)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!isMeasured) {
            arc.layoutParams = LayoutParams(fab.measuredWidth, fab.measuredHeight, Gravity.CENTER)
            status.layoutParams = LayoutParams(fab.measuredWidth, fab.measuredHeight, Gravity.CENTER)
            isMeasured = true
        }
    }

    fun setError() {
        arc.stop()
        status.setStatus(true)
    }

    fun setProgress() {
        arc.play()
    }

    fun setSuccess() {
        arc.stop()
        status.setStatus(false)
    }


    /**
     *
     */
    class StatusView(context: Context?) : View(context) {
        private var centerX: Float = 0f
        private var centerY: Float = 0f
        private var radius: Float = 0f
        private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var statusIcon: Drawable by Delegates.notNull()
        private var animation: ValueAnimator by Delegates.notNull()

        init {
            statusIcon = resources.getDrawable(R.drawable.ic_done_white_24px, null)

            paint.style = Paint.Style.FILL
            paint.color = 0xFFF97A05.toInt()
            paint.strokeCap = Paint.Cap.BUTT

            animation = ValueAnimator.ofFloat(0.0f, 1.0f)
            animation.duration = 400
            animation.interpolator = LinearInterpolator()
            animation.addUpdateListener { animation ->
                alpha = animation?.animatedValue as Float
                invalidate()
            }
            animation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    postDelayed({
                        visibility = GONE
                    }, 500)
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {
                    visibility = VISIBLE
                }
            })

            visibility = GONE
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = MeasureSpec.getSize(heightMeasureSpec)

            centerX = (width / 2).toFloat()
            centerY = (height / 2).toFloat()
            radius = if (centerX < centerY) centerX else centerY

            val factor = measuredWidth / 7
            statusIcon.bounds.set(factor * 2, factor * 2, width - factor * 2, height - factor * 2)

            setMeasuredDimension(width, height)
        }


        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            canvas?.drawCircle(centerX, centerY, radius, paint)
            statusIcon.draw(canvas)
        }

        fun setStatus(hasError: Boolean) {
            if (hasError) {
                statusIcon = resources.getDrawable(R.drawable.ic_warning_white_24px, null)
                paint.color = Color.RED
            } else {
                statusIcon = resources.getDrawable(R.drawable.ic_done_white_24px, null)
                paint.color = 0xFFF97A05.toInt()
            }
            animation.start()
        }
    }


    /**
     *
     */
    class ArcView(context: Context) : View(context) {
        private var mPaint: Paint by Delegates.notNull()
        private var mRect: RectF by Delegates.notNull()
        private var mStartAngle: Float by Delegates.notNull()
        private var mSweepAngle: Float by Delegates.notNull()
        private var startAngleAnim: ValueAnimator by Delegates.notNull()
        private var sweepAngleAnim: ValueAnimator by Delegates.notNull()
        private var anim: AnimatorSet by Delegates.notNull()
        private var density: Float by Delegates.notNull()

        init {
            density = resources.displayMetrics.density

            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint.color = 0xFFF97A05.toInt()
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = density * 2f
            mPaint.strokeCap = Paint.Cap.BUTT

            mRect = RectF()

            mStartAngle = -90f
            mSweepAngle = 0f

            startAngleAnim = ValueAnimator.ofFloat(-90f, 270f)
            startAngleAnim.duration = 3000
            startAngleAnim.repeatCount = -1
            startAngleAnim.interpolator = LinearInterpolator()
            startAngleAnim.addUpdateListener { anim ->
                val v = anim?.animatedValue as Float
                mStartAngle = v
            }

            sweepAngleAnim = ValueAnimator.ofFloat(10f, 360f)
            sweepAngleAnim.duration = 3000
            sweepAngleAnim.repeatCount = -1
            sweepAngleAnim.interpolator = LinearInterpolator()
            sweepAngleAnim.addUpdateListener { anim ->
                val v = anim?.animatedValue as Float
                mSweepAngle = v
                invalidate()
            }

            anim = AnimatorSet()
            anim.play(startAngleAnim).with(sweepAngleAnim)
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            val width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
            val height = MeasureSpec.getSize(heightMeasureSpec).toFloat()
            mRect.set(density, density, width - density, height - density)
            setMeasuredDimension(measuredWidth, measuredHeight)
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            canvas?.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint)
        }

        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            anim.cancel()
        }

        /**
         * play animation
         */
        fun play() {
            alpha = 1.0f
            anim.start()
        }

        /**
         * stop animation
         */
        fun stop() {
            alpha = 0.0f
            anim.cancel()
        }
    }
}