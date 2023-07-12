package com.example.mtmusic.util

import android.content.Context
import android.widget.Toast

object ToastUtil {
    private var mToast: Toast? = null

    fun showToast(context: Context, message: String) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        } else {
            mToast!!.setText(message)
        }
        mToast!!.show()
    }

    fun showToast(context: Context, resId: Int) {
        showToast(context, context.getString(resId))
    }
}