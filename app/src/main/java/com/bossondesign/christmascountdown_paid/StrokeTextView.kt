package com.bossondesign.christmascountdown_paid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class StrokeTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    private val strokePaint = Paint()

    init {
        // Copy original TextView's paint to the stroke
        strokePaint.set(paint)
        // Configure the stroke paint
        strokePaint.strokeWidth = 100f // Increase stroke width for visibility
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = Color.BLACK // Stroke color
        strokePaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        val textColor = textColors

        // Draw the stroke
        paint.style = Paint.Style.STROKE
        setTextColor(strokePaint.color)
        super.onDraw(canvas)

        // Draw the text
        paint.style = Paint.Style.FILL
        setTextColor(textColor)
        super.onDraw(canvas)
    }
}
