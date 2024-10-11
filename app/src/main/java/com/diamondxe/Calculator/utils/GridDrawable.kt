package com.dxe.calc.utils

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable


class GridDrawable(
    private val gridSize: Float,
    private val gridColor: Int,
    private val borderColor: Int,
    private val borderRadius: Float
) : Drawable() {
    private val gridPaint = Paint()
    private val borderPaint = Paint()
    private val rectF = RectF()
    private val path = Path()

    init {
        gridPaint.color = gridColor
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = 1f

        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 4f
    }

    override fun draw(canvas: Canvas) {
        val borderThickness = borderPaint.strokeWidth
        val halfBorderThickness = borderThickness / 2

        val width = bounds.width().toFloat() - borderThickness
        val height = bounds.height().toFloat() - borderThickness

        val left = halfBorderThickness
        val top = halfBorderThickness
        val right = width + halfBorderThickness
        val bottom = height + halfBorderThickness

        rectF.set(left, top, right, bottom)
        path.reset()
        path.addRoundRect(rectF, borderRadius, borderRadius, Path.Direction.CW)

        canvas.save()
        canvas.clipPath(path)

        val gridLeft = left
        val gridTop = top
        val gridRight = right
        val gridBottom = bottom
        var x = gridLeft
        while (x <= gridRight) {
            canvas.drawLine(x, gridTop, x, gridBottom, gridPaint)
            x += gridSize
        }


        var y = gridTop
        while (y <= gridBottom) {
            canvas.drawLine(gridLeft, y, gridRight, y, gridPaint)
            y += gridSize
        }

        canvas.restore()
        canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint)
    }

    override fun setAlpha(alpha: Int) {
        gridPaint.alpha = alpha
        borderPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        gridPaint.colorFilter = colorFilter
        borderPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

}






/*  work fine code
class GridDrawable(private val gridSize: Float,private val gridColor: Int, private val borderColor: Int,
                   private val borderRadius: Float,
                   ) : Drawable() {
    private val paint = Paint()
    private val borderPaint = Paint()
    private val rectF = RectF()
    init {
        paint.color = gridColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f // Customize the thickness of the grid lines

        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 5f
    }

    override fun draw(canvas: Canvas) {
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        // Draw vertical grid lines
        var x = 0f
        while (x < width) {
            canvas.drawLine(x, 0f, x, height, paint)
            x += gridSize
        }

        // Draw horizontal grid lines
        var y = 0f
        while (y < height) {
            canvas.drawLine(0f, y, width, y, paint)
            y += gridSize
        }

        rectF.set(0f, 0f, width, height)
        canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
*/
