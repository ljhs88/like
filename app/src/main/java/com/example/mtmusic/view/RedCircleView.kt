package com.example.mtmusic.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import java.lang.Math.cos
import java.lang.Math.sin

class RedCircleView(context: Context, attrs: AttributeSet?) : View(context, attrs), View.OnClickListener {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var clipPath: Path? = null

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        setOnClickListener(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = 0f
        clipPath = Path().apply {
            addCircle(centerX, centerY, radius, Path.Direction.CW)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.clipPath(clipPath!!)
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, width / 2f)
        animator.duration = 100
        animator.interpolator = AccelerateInterpolator()
        animator.addUpdateListener { animation ->
            radius = animation.animatedValue as Float
            clipPath?.reset()
            clipPath?.addCircle(centerX, centerY, radius, Path.Direction.CW)
            invalidate()
        }
        animator.start()
    }

    override fun onClick(v: View?) {
        startAnimation()
    }
}

