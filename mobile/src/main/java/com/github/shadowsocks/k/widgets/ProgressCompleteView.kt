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
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.LinearInterpolator
import com.github.shadowsocks.k.R
import kotlin.properties.Delegates

/**
 * hello,kotlin
 * Created by lhyz on 2017/6/2.
 */
class ProgressCompleteView(context: Context?) : View(context) {
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var doneIcon: Drawable by Delegates.notNull()
    private var animation: ValueAnimator by Delegates.notNull()

    init {
        doneIcon = resources.getDrawable(R.drawable.ic_done_white_24px, null)

        paint.style = Paint.Style.FILL
        paint.color = 0xFFF97A05.toInt()
        paint.strokeCap = Paint.Cap.BUTT

        animation = ValueAnimator.ofFloat(0f, 1f)
        animation.duration = 1000
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
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        radius = if (centerX < centerY) centerX else centerY

        val factor = measuredWidth / 7
        doneIcon.bounds.set(factor * 2, factor * 2, width - factor * 2, height - factor * 2)

        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(centerX, centerY, radius, paint)
        doneIcon.draw(canvas)
    }

    fun startAnimation() {
        animation.start()

    }

    fun stopAnimation() {
        animation.cancel()
    }
}