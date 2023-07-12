package com.example.mtmusic.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.BitmapCompat

object compressBitmap {
    // 压缩图片
    fun compressionBitmap(
        resources: Resources?,
        pictureId: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true //仅获取图片尺寸，不加载图片内容
        BitmapFactory.decodeResource(resources, pictureId, options)
        // 计算采样率
        val width = options.outWidth
        val height = options.outHeight
        val sampleSize = Math.max(
            width / targetWidth,
            height / targetHeight
        ) // targetWidth，targetHeight目标宽高
        options.inSampleSize = sampleSize
        options.inJustDecodeBounds = false //获取完图片尺寸后再加载图片内容
        return BitmapFactory.decodeResource(resources, pictureId, options).copy(Bitmap.Config.ARGB_8888, true)
    }
}