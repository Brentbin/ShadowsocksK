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

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import kotlin.properties.Delegates

/**
 * hello,kotlin
 * Created by lhyz on 2017/5/30.
 *
 *  A circular loader may be integrated with a floating action button.
 *
 * implement of https://material.io/guidelines/components/progress-activity.html#progress-activity-types-of-indicators
 */
class CircularLoader(context: Context, attrs: AttributeSet) :
        FrameLayout(context, attrs),
        ProgressArcView.ArchListener {

    private var isMeasured = false
    private val density = resources.displayMetrics.density
    private var fab: FloatingActionButton by Delegates.notNull()
    private var arc: ProgressArcView by Delegates.notNull()
    private var done: ProgressCompleteView by Delegates.notNull()
    private var isStart: Boolean = false
    private var isCompleted: Boolean = true

    var listener: OnProgressListener? = null

    init {
        setOnClickListener {
            if (!isStart) {
                arc.startAnimation()
            }
            if (!isCompleted) {
                arc.stopAnimation()
                done.stopAnimation()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        fab = getChildAt(0) as FloatingActionButton
        fab.isClickable = false
        val params = fab.layoutParams as LayoutParams
        params.gravity = Gravity.CENTER

        arc = ProgressArcView(context)
        arc.isClickable = false
        addView(arc, LayoutParams(144, 144, Gravity.CENTER))
        ViewCompat.setElevation(arc, ViewCompat.getElevation(fab) + 1)
        arc.listener = this

        done = ProgressCompleteView(context)
        done.isClickable = false
        addView(done, LayoutParams(144, 144, Gravity.CENTER))
        ViewCompat.setElevation(done, ViewCompat.getElevation(fab) + 2)
        done.visibility = View.GONE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!isMeasured) {
            arc.layoutParams = LayoutParams((fab.measuredWidth + 8 * density).toInt(), (fab.measuredHeight + 8 * density).toInt(), Gravity.CENTER)
            arc.strokeWidth = 4 * density
            done.layoutParams = LayoutParams(fab.measuredWidth, fab.measuredHeight, Gravity.CENTER)
            isMeasured = true
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        arc.stopAnimation()
        done.stopAnimation()
    }

    override fun onCancel() {
        isStart = false
        isCompleted = true
        listener?.onCancel()
    }

    override fun onEnd() {
        isStart = false
        isCompleted = true
        listener?.onCompleted()
        done.startAnimation()
    }

    override fun onStart() {
        isStart = true
        isCompleted = false
        listener?.onStart()
    }


    interface OnProgressListener {
        fun onStart()
        fun onCancel()
        fun onCompleted()
    }
}