package com.example.testclockapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class AnalogClockCustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val numbersArabicStyle =
        arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private val numbersRomanStyle =
        arrayOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII")
    private val numbers = numbersArabicStyle
    private val rect: Rect = Rect()
    private val paint = Paint()
    private var size = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawClockBackground(canvas)
        drawArrows(canvas)
        drawClockCenter(canvas)
        postInvalidateDelayed(1000);
        invalidate();
    }

    private fun drawClockBackground(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = size / 30
        canvas.drawCircle(size, size, size / 1.035f, paint)
        paint.strokeWidth = size / 150
        canvas.drawCircle(size, size, size / 1.08f, paint)
        drawNumbers(canvas)
    }

    private fun drawNumbers(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.textSize = size / 5
        paint.style = Paint.Style.FILL
        for (item in numbers) {
            paint.getTextBounds(item, 0, item.length, rect)
            val angle = Math.PI / 6 * (numbers.indexOf(item) - 2)
            val x = (size + cos(angle) * size / 1.3 - rect.width() / 2)
            val y = (size + sin(angle) * size / 1.3 + rect.height() / 2)
            canvas.drawText(item, x.toFloat(), y.toFloat(), paint)
        }
    }

    private fun drawArrows(canvas: Canvas) {
        val calendar: Calendar = Calendar.getInstance()
        val currentSecond = calendar.get(Calendar.SECOND).toFloat()
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentMinuteImproved = (currentMinute + currentSecond / 60)
        val currentHour = calendar.get(Calendar.HOUR)
        val currentHourFormat = 5 * (if (currentHour > 12) currentHour - 12 else currentHour)
        val currentHourImproved = (currentHourFormat + currentMinuteImproved / 60 * 5)

        drawSingleArrow(canvas, currentHourImproved, Color.BLACK, 0.5f, 20)
        drawSingleArrow(canvas, currentMinuteImproved, Color.BLACK, 0.7f, 10)
        drawSingleArrow(canvas, currentSecond, Color.RED, 0.8f, 5)
    }

    private fun drawSingleArrow(
        canvas: Canvas,
        arrowPosition: Float,
        arrowColor: Int,
        lineLength: Float,
        lineWidth: Int
    ) {
        paint.color = arrowColor
        paint.strokeWidth = size / 300 * lineWidth.toFloat()
        val angle = Math.PI * arrowPosition / 30 - Math.PI / 2
        canvas.drawLine(
            size, size,
            (size + cos(angle) * (size * lineLength)).toFloat(),
            (size + sin(angle) * (size * lineLength)).toFloat(),
            paint
        )
        val angleOppositeLine = Math.PI * arrowPosition / 30 - Math.PI / 2 - Math.PI
        canvas.drawLine(
            size, size,
            (size + cos(angleOppositeLine) * (size / 10)).toFloat(),
            (size + sin(angleOppositeLine) * (size / 10)).toFloat(),
            paint
        )
    }

    private fun drawClockCenter(canvas: Canvas) {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas.drawCircle(size, size, size / 25, paint)
        drawNumbers(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = measuredWidth.coerceAtMost(measuredHeight) / 2f
        setMeasuredDimension((size * 2).toInt(), (size * 2).toInt())
    }
}