package com.example.mtmusic.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import java.util.Locale

class MyClock : View {
    /**
     * 时钟
     */
    var mHour = 0
    var mMinute = 0
    var mSecond = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2
        val cy = height / 2
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        val radius = Math.min(cx, cy) / 2
        val shader: Shader = RadialGradient(
            cx.toFloat(), cy.toFloat(), radius.toFloat(), Color.parseColor("#E91E63"),
            Color.parseColor("#2196F3"), Shader.TileMode.CLAMP
        )
        paint.shader = shader

        // 绘制表框
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius.toFloat(), paint)

        // 绘制时刻线
        paint.strokeWidth = 5f
        for (i in 0..11) {
            val x1 = (cx + (radius - 20) * Math.sin(Math.PI / 6 * i)).toInt()
            val y1 = (cy - (radius - 20) * Math.cos(Math.PI / 6 * i)).toInt()
            val x2 = (cx + radius * Math.sin(Math.PI / 6 * i)).toInt()
            val y2 = (cy - radius * Math.cos(Math.PI / 6 * i)).toInt()
            canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), paint)
        }
        setTime()
        val time = translateTime(mHour, mMinute)
        paint.textSize = 80f
        paint.textAlign = Paint.Align.CENTER
        paint.textLocale = Locale.CHINA
        canvas.drawText(time, cx.toFloat(), (cy + radius + 200).toFloat(), paint)

        // 绘制时针、分针、秒针
        paint.strokeWidth = 10f // 宽度
        val hx =
            (cx + (radius - 150) * Math.sin(Math.PI / 6 * mHour + Math.PI / 360 * mMinute)).toInt()
        val hy =
            (cy - (radius - 150) * Math.cos(Math.PI / 6 * mHour + Math.PI / 360 * mMinute)).toInt()
        canvas.drawLine(cx.toFloat(), cy.toFloat(), hx.toFloat(), hy.toFloat(), paint)
        paint.strokeWidth = 5f
        val mx = (cx + (radius - 100) * Math.sin(Math.PI / 30 * mMinute)).toInt()
        val my = (cy - (radius - 100) * Math.cos(Math.PI / 30 * mMinute)).toInt()
        canvas.drawLine(cx.toFloat(), cy.toFloat(), mx.toFloat(), my.toFloat(), paint)
        paint.strokeWidth = 3f
        val sx = (cx + (radius - 50) * Math.sin(Math.PI / 30 * mSecond)).toInt()
        val sy = (cy - (radius - 50) * Math.cos(Math.PI / 30 * mSecond)).toInt()
        canvas.drawLine(cx.toFloat(), cy.toFloat(), sx.toFloat(), sy.toFloat(), paint)

        // 绘制中心小圆点
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), 5f, paint)
    }

    fun setTime() {
        //获取当前时间
        val calendar = Calendar.getInstance()
        mHour = calendar[Calendar.HOUR_OF_DAY] //获取小时数
        mMinute = calendar[Calendar.MINUTE] //获取分钟数
        mSecond = calendar[Calendar.SECOND] //获取秒数
        invalidate()
    }

    private fun translateTime(mHour: Int, mMinute: Int): String {
        val timeSb = StringBuilder()
        if (mHour < 10) {
            timeSb.append(" ")
        }
        timeSb.append(mHour).append(":")
        if (mMinute < 10) {
            timeSb.append("0")
        }
        timeSb.append(mMinute)
        return timeSb.toString()
    }
}