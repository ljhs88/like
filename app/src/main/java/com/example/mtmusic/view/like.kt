package com.example.mtmusic.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.addListener
import com.example.mtmusic.R

/*从未点赞到点赞变化是：
a.灰色拇指变小，b.再变成彩色拇指，从刚才的大小变到原始大小，同时c.彩色圆圈从中间扩散开，
扩散的时候散开的点也逐渐显示出来（使用clipPath实现）；
动画执行顺序是先执行a再b,c一起执行；*/
class like: View, View.OnClickListener{

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mClipPath: Path? = null
    private var mShiningPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 记录点赞状态
    var isDown = true

    // 三张图片
    private var usLike: Bitmap? = null
    private var sLike: Bitmap? = null
    private var shine: Bitmap? = null
    private var mBitmap: Bitmap? = null

    // 记录动画执行时间
    private var animator_time = 0L

    // 点赞图片缩放时间
    private val SCALE_DURATION = 150f

    // 赞图片恢复时间 = 彩圈扩散时间
    private val RADIUS_DURATION = 100f

    // 点赞缩放比例
    private val SCALE_MIN = 0.9f
    private val SCALE_MAX = 1f
    private var mScale = 1f

    // 圆圈坐标
    private var circleCenterX = 0f
    private var circleCenterY = 0f

    //圆圈颜色
    private val START_COLOR = Color.parseColor("#00e24d3d")
    private val END_COLOR = Color.parseColor("#88e24d3d")
    private var mColor = START_COLOR

    //圆圈扩散的最小最大值，根据图标大小计算得出
    private val RADIUS_MIN = 0f
    private val RADIUS_MAX = 45f
    private var mRadius = 0f

    //彩点alpha
    private val ALPHA_MIN = 0
    private val ALPHA_MAX = 255
    private var mAlpha = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {}

    init {
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.like_unselected)
        usLike = bitmap1.copy(Bitmap.Config.ARGB_8888, true)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.like_selected)
        sLike = bitmap2.copy(Bitmap.Config.ARGB_8888, true)
        val bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.like_selected_shining)
        shine = bitmap3.copy(Bitmap.Config.ARGB_8888, true)

        mBitmap = if (isDown) {
            usLike
        } else {
            sLike
        }

        mCirclePaint.color = Color.RED
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = 5f

        mShiningPaint.alpha = mAlpha

        setOnClickListener(this)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bitmapWidth = usLike!!.width  // 90
        val bitmapHeight = usLike!!.height// 90
        val centerX = (width/2).toFloat()
        val centerY = (height/2).toFloat()
        val x = centerX - bitmapWidth/2
        val y = centerY - bitmapHeight/2

        drawUsLike(canvas, x, y)
//        drawCircle(canvas)
        drawShining(canvas, x, y)
    }

    // 点赞
    private fun drawUsLike(canvas: Canvas, x: Float, y: Float) {
        if (usLike != null && sLike != null) {
            canvas.save()
            canvas.scale(mScale, mScale)
            canvas.drawBitmap(mBitmap!!, x, y, paint)
            canvas.restore()
        }
    }

    // 彩圈
    private fun drawCircle(canvas: Canvas) {
        canvas.clipPath(mClipPath!!)
        canvas.drawCircle(circleCenterX, circleCenterY, mRadius, mCirclePaint)
    }

    // 彩点
    private fun drawShining(canvas: Canvas, x: Float, y: Float) {
        canvas.save()
        canvas.drawBitmap(shine!!, x,
            y- usLike!!.width/2, mShiningPaint)
        canvas.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        circleCenterX = w / 2f
        circleCenterY = h / 2f

        mClipPath = Path().apply {
            addCircle(circleCenterX, circleCenterY, mRadius, Path.Direction.CW)
        }
    }

    /*--------------点赞动画--------------*/
    // 缩小动画
    private fun usLikeScaleToMinAnimation() {
        val animatorScale = ObjectAnimator.ofFloat(
            this,
            "scale", SCALE_MAX, SCALE_MIN)
        animatorScale.duration = SCALE_DURATION.toLong()
        animatorScale.start()
    }

    // 放大动画
    private fun usLikeScaleToMaxAnimation() {
        val animatorScale = ObjectAnimator.ofFloat(this,
            "scale", SCALE_MIN, SCALE_MAX)

        val animatorRadius = ObjectAnimator.ofFloat(this,
            "radius", RADIUS_MIN, RADIUS_MAX)
        animatorRadius.interpolator = AccelerateInterpolator()

        val animatorAlpha = ObjectAnimator.ofInt(this,
            "alpha", ALPHA_MIN, ALPHA_MAX)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorScale, animatorRadius, animatorAlpha)
        animatorSet.duration = RADIUS_DURATION.toLong()
        animatorSet.startDelay = SCALE_DURATION.toLong()
        animatorSet.addListener(onEnd = {
            isDown = false
        })
        animatorSet.start()
    }

    /*-----------取消点赞动画-----------*/
    // 彩点需要和缩小同步
    private fun usLikeScaleToMinAnimation2() {
        val animatorScale = ObjectAnimator.ofFloat(
            this,
            "scale", SCALE_MAX, SCALE_MIN)

        val animatorAlpha = ObjectAnimator.ofInt(this,
            "alpha", ALPHA_MAX, ALPHA_MIN)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorAlpha, animatorScale)
        animatorSet.duration = SCALE_DURATION.toLong()
        animatorSet.start()
    }

    // 放大无彩点
    // 放大动画
    private fun usLikeScaleToMaxAnimation2() {
        val animatorScale = ObjectAnimator.ofFloat(this,
            "scale", SCALE_MIN, SCALE_MAX)
        animatorScale.duration = RADIUS_DURATION.toLong()
        animatorScale.startDelay = SCALE_DURATION.toLong()
        animatorScale.addListener(onEnd = {
            isDown = true
        })
        animatorScale.start()
    }

    // 设置图片
    private fun setScale(scale: Float) {
        mScale = scale
        mBitmap = if (isDown) {
            if (animator_time + SCALE_DURATION < System.currentTimeMillis()) {
                usLike
            } else {
                sLike
            }
        } else {
            if (animator_time + SCALE_DURATION < System.currentTimeMillis()) {
                sLike
            } else {
                usLike
            }
        }
        invalidate()
    }

    // 设置半径
    private fun setRadius(radius: Float) {
        mRadius = radius
        mClipPath!!.reset()
        mClipPath!!.addCircle(circleCenterX, circleCenterY, mRadius, Path.Direction.CW)

//        val fraction = (RADIUS_MAX - radius) / (RADIUS_MAX - RADIUS_MIN)
//        mCirclePaint.color = evaluate(fraction, Color.RED, Color.WHITE) as Int
        invalidate()
    }

    private fun setAlpha(alpha: Int) {
        mAlpha = alpha
        mShiningPaint.alpha = mAlpha
    }

    fun evaluate(fraction: Float, startValue: Any, endValue: Any): Any? {
        val startInt = startValue as Int
        val startA = (startInt shr 24 and 0xff) / 255.0f
        var startR = (startInt shr 16 and 0xff) / 255.0f
        var startG = (startInt shr 8 and 0xff) / 255.0f
        var startB = (startInt and 0xff) / 255.0f
        val endInt = endValue as Int
        val endA = (endInt shr 24 and 0xff) / 255.0f
        var endR = (endInt shr 16 and 0xff) / 255.0f
        var endG = (endInt shr 8 and 0xff) / 255.0f
        var endB = (endInt and 0xff) / 255.0f

        // convert from sRGB to linear
        startR = Math.pow(startR.toDouble(), 2.2).toFloat()
        startG = Math.pow(startG.toDouble(), 2.2).toFloat()
        startB = Math.pow(startB.toDouble(), 2.2).toFloat()
        endR = Math.pow(endR.toDouble(), 2.2).toFloat()
        endG = Math.pow(endG.toDouble(), 2.2).toFloat()
        endB = Math.pow(endB.toDouble(), 2.2).toFloat()

        // compute the interpolated color in linear space
        var a = startA + fraction * (endA - startA)
        var r = startR + fraction * (endR - startR)
        var g = startG + fraction * (endG - startG)
        var b = startB + fraction * (endB - startB)

        // convert back to sRGB in the [0..255] range
        a = a * 255.0f
        r = Math.pow(r.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
        g = Math.pow(g.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
        b = Math.pow(b.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
        return Math.round(a) shl 24 or (Math.round(r) shl 16) or
                (Math.round(g) shl 8) or Math.round(b)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (usLike != null) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = (width * usLike!!.height / usLike!!.width)
            setMeasuredDimension(width, height)
        }
    }

    override fun onClick(v: View?) {
        // 记录开始执行动画的时间
        animator_time = System.currentTimeMillis()
        if (isDown) {
            // 执行点赞动画
            likeAnimation()
        } else {
            // 执行取消点赞动画
            cancelLikeAnimation()
        }
    }

    private fun likeAnimation() {
        usLikeScaleToMinAnimation()
        usLikeScaleToMaxAnimation()
//        isDown = false
    }

    private fun cancelLikeAnimation() {
        usLikeScaleToMinAnimation2()
        usLikeScaleToMaxAnimation2()
//        isDown = true
    }

}