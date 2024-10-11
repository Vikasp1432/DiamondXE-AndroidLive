package com.dxe.calc.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.diamondxe.R


class SquareView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var selectedDiameter = 13f
    private var selectedIND = 0

    fun adjustSquareSize(diameterMM: Float, ind: Int) {
        selectedDiameter = diameterMM
        selectedIND = ind
        invalidate()
    }

    private val squareborder = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_D4AA60)
        strokeWidth = 5f
    }


    private val arrowPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_D4AA60)
        strokeWidth = 8f
    }

    private val innerPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_B3FFFFFF)
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_D4AA60)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.black)
        textSize = 35f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("lato_regular", Typeface.BOLD)
    }

    private val textArrowPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_7E3080)
        textSize = 35f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("lato_regular", Typeface.BOLD)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val dpi = resources.displayMetrics.densityDpi.toFloat()
        val widthPx = width.toFloat()
        val heightPx = height.toFloat()
        val diameterPx = selectedDiameter * (dpi / 25.4f)
        val top = (heightPx / 2f) - (diameterPx / 2f)
        val bottom = (heightPx / 2f) + (diameterPx / 2f)
        Log.e("Square Coordinates", "Top: $top, Bottom: $bottom")
        val left = 0f

        // Draw the rectangle
        canvas.drawRect(left, top, widthPx, bottom, innerPaint)

        val textMargin = 10f
        // Draw the top border
        canvas.drawLine(left, top, widthPx, top, borderPaint)
        // Draw the bottom border
        canvas.drawLine(left, bottom, widthPx, bottom, borderPaint)
        val effectiveHeight = bottom - top - (2 * textMargin)
        val textY = top + (effectiveHeight / 2) + textMargin

        val startX = widthPx / 2f
        canvas.drawText(
            context.getString(R.string.place_finger),
            startX,
            textY,
            textPaint
        )

        val margin = 75f
        val arrowY = (top + bottom) / 2f
        drawArrow(canvas, margin + 5f, arrowY, diameterPx, isUp = true, offset = 5f) // Left top arrow
        drawArrow(canvas, widthPx - margin - 5f, arrowY, diameterPx, isUp = true, offset = 5f) // Right top arrow
        val topTextY = arrowY
        canvas.drawText("$selectedIND- IND", margin + 10f, topTextY, textArrowPaint)
        canvas.drawText("$selectedIND- IND", widthPx - margin - 5f, topTextY, textArrowPaint)


        drawArrow(canvas, margin + 5f, arrowY, diameterPx, isUp = false, offset = 5f) // Left bottom arrow
        drawArrow(canvas, widthPx - margin - 5f, arrowY, diameterPx, isUp = false, offset = 5f) // Right bottom arrow


    }


    private fun drawArrow(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radiusPx: Float,
        isUp: Boolean,
        offset: Float
    ) {
        // Adjusted lengths for smaller arrows
        val arrowLength = radiusPx / 5 // Smaller length for the arrow
        val fixedArrowHeight = 18f // Fixed height for the arrow head

        // Calculate the arrow tip position
        val arrowTipX = centerX
        val arrowTipY = centerY + (if (isUp) -arrowLength - offset else arrowLength + offset)

        // Draw the vertical line of the arrow
        canvas.drawLine(
            arrowTipX, arrowTipY,
            arrowTipX, arrowTipY + (if (isUp) -arrowLength else arrowLength),
            arrowPaint
        )

        // Draw the arrow head (left and right parts)
        val headBaseX = arrowTipX
        val headBaseY = arrowTipY + (if (isUp) -arrowLength else arrowLength)
        val arrowHeadLeftX = headBaseX - fixedArrowHeight
        val arrowHeadRightX = headBaseX + fixedArrowHeight

        canvas.drawLine(
            headBaseX, headBaseY,
            arrowHeadLeftX, headBaseY + (if (isUp) fixedArrowHeight else -fixedArrowHeight),
            arrowPaint
        )

        canvas.drawLine(
            headBaseX, headBaseY,
            arrowHeadRightX, headBaseY + (if (isUp) fixedArrowHeight else -fixedArrowHeight),
            arrowPaint
        )
    }


    /*private fun drawArrow(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radiusPx: Float,
        isUp: Boolean,
        offset: Float
    ) {
        // Adjusted lengths for smaller arrows
        val arrowLength = radiusPx / 5 // Smaller length for the arrow
        val arrowHeight = radiusPx / 14 // Smaller height for the arrow head

        // Calculate the arrow tip position
        val arrowTipX = centerX
        val arrowTipY = centerY + (if (isUp) -arrowLength - offset else arrowLength + offset)

        // Draw the vertical line of the arrow
        canvas.drawLine(
            arrowTipX, arrowTipY,
            arrowTipX, arrowTipY + (if (isUp) -arrowLength else arrowLength),
            arrowPaint
        )

        // Draw the arrow head (left and right parts)
        val headBaseX = arrowTipX
        val headBaseY = arrowTipY + (if (isUp) -arrowLength else arrowLength)
        val arrowHeadLeftX = headBaseX - arrowHeight
        val arrowHeadRightX = headBaseX + arrowHeight

        canvas.drawLine(
            headBaseX, headBaseY,
            arrowHeadLeftX, headBaseY + (if (isUp) arrowHeight else -arrowHeight),
            arrowPaint
        )

        canvas.drawLine(
            headBaseX, headBaseY,
            arrowHeadRightX, headBaseY + (if (isUp) arrowHeight else -arrowHeight),
            arrowPaint
        )
    }*/


}


