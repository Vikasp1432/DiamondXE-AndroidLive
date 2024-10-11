package com.dxe.calc.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.diamondxe.R

class CircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {


    private var selectedDiameter = 13f
    private var selectedIND=0

    fun adjustCircleSize(diameterMM: Float,ind:Int) {
        selectedDiameter = diameterMM
        selectedIND=ind
        invalidate()
    }

    private val arrowPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_D4AA60)// Color for the arrows
        strokeWidth = 5f // Thickness of the arrows
    }

    private val innerPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_B3FFFFFF) // Set inner color
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_D4AA60) // Set border color
        style = Paint.Style.STROKE
        strokeWidth = 3f // Border thickness
    }

    private val textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_7E3080)
        textSize = 38f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("lato_regular", Typeface.BOLD) // Using a built-in sans-serif medium typeface
    }


    /*private fun drawArrow(canvas: Canvas, centerX: Float, centerY: Float, radiusPx: Float, isLeft: Boolean, offset: Float) {
        // Arrow length and height
        val arrowLength = radiusPx / 5  // Length of the arrow lines
        val arrowHeight = radiusPx / 5   // Height of the arrowheads

        // Calculate the position of the arrow tip at the circle's edge with an offset
        val arrowTipX = centerX + (if (isLeft) -radiusPx + arrowLength + offset else radiusPx - arrowLength - offset)
        val arrowTipY = centerY

        // Draw the arrow line (adjusted to start inside the circle)
        canvas.drawLine(
            arrowTipX, arrowTipY,
            arrowTipX + (if (isLeft) -arrowLength else arrowLength), // Length towards the inside
            arrowTipY,
            arrowPaint
        )

        // Draw the arrow head
        // Left arrow head
        canvas.drawLine(
            arrowTipX + (if (isLeft) -arrowLength else arrowLength),
            arrowTipY,
            arrowTipX + (if (isLeft) -arrowLength + (arrowHeight / 2) else arrowLength - (arrowHeight / 2)),
            arrowTipY + arrowHeight,
            arrowPaint
        )

        // Right arrow head
        canvas.drawLine(
            arrowTipX + (if (isLeft) -arrowLength else arrowLength),
            arrowTipY,
            arrowTipX + (if (isLeft) -arrowLength + (arrowHeight / 2) else arrowLength - (arrowHeight / 2)),
            arrowTipY - arrowHeight,
            arrowPaint
        )
    }*/



    private fun drawArrow(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radiusPx: Float,
        isLeft: Boolean,
        offset: Float
    ) {
        // Arrow length and fixed height for the arrow head
        val arrowLength = radiusPx / 3 // Length for the arrow
        val fixedArrowHeight = 18f // Fixed height for the arrow head

        // Calculate the position of the arrow tip at the circle's edge with an offset
        val arrowTipX = centerX + (if (isLeft) -radiusPx + arrowLength + offset else radiusPx - arrowLength - offset)
        val arrowTipY = centerY

        // Draw the arrow line (adjusted to start inside the circle)
        canvas.drawLine(
            arrowTipX, arrowTipY,
            arrowTipX + (if (isLeft) -arrowLength else arrowLength), // Length towards the inside
            arrowTipY,
            arrowPaint
        )

        // Calculate the base position of the arrowhead
        val headBaseX = arrowTipX + (if (isLeft) -arrowLength else arrowLength)
        val headBaseY = arrowTipY

        // Calculate the points for the fixed height arrowhead
        val arrowHeadRightX = headBaseX + (if (isLeft) fixedArrowHeight else -fixedArrowHeight) // Move left or right for the tip
        val arrowHeadTopY = headBaseY + (fixedArrowHeight / 1.1f) // Height for the top part of the head
        val arrowHeadBottomY = headBaseY - (fixedArrowHeight / 1.1f) // Height for the bottom part of the head

        // Draw the arrowhead
        canvas.drawLine(headBaseX, headBaseY, arrowHeadRightX, arrowHeadTopY, arrowPaint) // Top part of the arrowhead
        canvas.drawLine(headBaseX, headBaseY, arrowHeadRightX, arrowHeadBottomY, arrowPaint) // Bottom part of the arrowhead
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //25.4f in the code is used to convert millimeters to pixels.
        val dpi = resources.displayMetrics.densityDpi.toFloat()
        val startX = width / 2f
        val startY = height / 2f
        val diameterPx = selectedDiameter * (dpi / 25.4f)
        val radiusPx = diameterPx / 2f

        // Draw the circle with the selected diameter
        // canvas.drawCircle(startX, radiusPx, radiusPx, paint)
        canvas.drawCircle(startX, startY, radiusPx, innerPaint)

        // Draw the border (outline) of the circle
        canvas.drawCircle(startX, startY, radiusPx, borderPaint)
        //canvas.drawCircle(startX, startY, radiusPx, paint)

        // convert this logic for square
        canvas.drawText(
            "$selectedIND -IND",
            startX,
            startY + (textPaint.textSize / 3),
            textPaint
        )


        val lineLength = radiusPx * 0.8f // Adjust this to control line size relative to circle radius
        val lineStartX = startX - lineLength
        val lineEndX = startX + lineLength

        /*canvas.drawLine(
            lineStartX, startY,  // Start point of the line
            lineEndX, startY,    // End point of the line
            borderPaint          // Paint to style the line (reuse borderPaint or define a new one)
        )*/

        val offset = 12f // Adjust this value to move the arrows away from the circle border
        drawArrow(canvas, startX, startY, radiusPx, true, offset)  // Left arrow
        drawArrow(canvas, startX, startY, radiusPx, false, offset) // Right arrow
    }
}